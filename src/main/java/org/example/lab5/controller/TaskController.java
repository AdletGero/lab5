package org.example.lab5.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.lab5.Entity.Category;
import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.TaskRepository;
import org.example.lab5.Repository.UserRepository;
import org.example.lab5.Service.CategoryService;
import org.example.lab5.Service.TaskService;
import org.example.lab5.Service.UserService;
import org.example.lab5.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@NoArgsConstructor
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;

    @GetMapping("add")
    public String showAddTask(Model model){
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "add";
    }
    @PostMapping("add")
    public String createTask(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("categoryId") Long categoryId,
                             Task task,
                             @RequestParam("userId") Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("Unknown login"));
        task.setUser(currentUser);

        Category category = categoryService.findCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));

        task.setCategory(category);
        taskService.saveTask(task);
        return "redirect:/admin/main";
    }
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.findAllCategories());
        return "edit";  // предполагается, что шаблон находится в resources/templates/edit.html
    }

    // Метод для сохранения отредактированной задачи
    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable("id") Long id, @ModelAttribute("task") Task task, @AuthenticationPrincipal UserDetails userDetails) {
        task.setId(id);
        User currentUser =  userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException("Unknown login"));
        task.setUser(currentUser);
        Optional<Category> category = categoryService.findCategoryById(task.getCategory().getId());
        task.setCategory(category.orElse(null));
        taskService.saveTask(task);
        return "redirect:/";
    }


}
