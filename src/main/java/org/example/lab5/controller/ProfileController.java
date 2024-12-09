package org.example.lab5.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.lab5.Entity.User;
import org.example.lab5.Service.UserService;
import org.example.lab5.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@AllArgsConstructor
@NoArgsConstructor
public class ProfileController {
    @Autowired
    UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/profile")
    public String profile(Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userService.findById(userId);
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam(value = "password", required = false) String password) throws IOException {


        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userService.findById(userId);
        userService.updateUserName(userId, updatedUser.getName());
        userService.updateUserEmail(userId, updatedUser.getEmail());
        if (image != null && !image.isEmpty()) {
            String imageName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path imagePath = uploadDir.resolve(imageName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            Path targetDir = Paths.get("target/classes/static/images");
            if (Files.notExists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path targetPath = targetDir.resolve(imageName);
            Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            userService.updateUserImageUrl(userId, imageName);
        }
        if (password != null && !password.isEmpty()) {
            userService.updateUserPassword(userId, userService.encodePassword(password));
        }

        return "redirect:/profile";
    }

}
