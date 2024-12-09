package org.example.lab5.controller;

import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.TaskRepository;
import org.example.lab5.Service.CategoryService;
import org.example.lab5.Service.TaskService;
import org.example.lab5.Service.UserService;
import org.example.lab5.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @GetMapping("/")
    public String main(@RequestParam(value = "status", required = false, defaultValue = "") String status,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       @RequestParam(value = "search", required = false, defaultValue = "") String search,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userService.findById(userId);
        if (currentUser == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }


        String statusFilter = status.equalsIgnoreCase("all") || status.isEmpty() ? null : status;

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        Page<Task> tasksPage = taskService.findTasksWithPaginationAndSearch(statusFilter, categoryId, userId, search, pageable);
        String def = "/images/"  + (currentUser.getImage_url() != null ? currentUser.getImage_url() : "default.png");

        model.addAttribute("tasks", tasksPage.getContent());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("status", status);
        model.addAttribute("category", categoryId);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksPage.getTotalPages());
        model.addAttribute("user", currentUser);
        model.addAttribute("default", def);

        return "main";
    }





    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTaskById(id);
        return "redirect:/";
    }
//    @GetMapping("/profile")
//    public String profile(Model model){
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//        Long userId = userDetails.getId();
//        User currentUser = userService.findById(userId);
//        model.addAttribute("user", currentUser);
//        return "profile";
//    }
//    @PostMapping("/profile")
//    public String updateProfile(
//            @ModelAttribute("user")@Validated User updatedUser,
//            BindingResult bindingResult,
//            Model model) {
//
//        // Проверка ошибок валидации
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("error", "Validation errors occurred!");
//            return "profile";
//        }
//
//        // Получаем текущего пользователя из базы
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User currentUser = userService.findById(userDetails.getId());
//
//        // Обновляем данные
//        currentUser.setName(updatedUser.getName());
//        currentUser.setEmail(updatedUser.getEmail());
//        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
//            currentUser.setPassword(encoder().encode(updatedUser.getPassword()));
//        }
//        if (updatedUser.getImage_url() != null && !updatedUser.getImage_url().isEmpty()) {
//            currentUser.setImage_url(updatedUser.getImage_url());
//        }
//
//        // Сохраняем изменения
//        userService.SaveUser(currentUser);
//
//        // Редирект на профиль с уведомлением
//        model.addAttribute("success", "Profile updated successfully!");
//        return "redirect:/profile";
//    }

}
