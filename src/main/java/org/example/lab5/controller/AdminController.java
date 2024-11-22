package org.example.lab5.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.lab5.Entity.Category;
import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Service.CategoryService;
import org.example.lab5.Service.EmailService;
import org.example.lab5.Service.TaskService;
import org.example.lab5.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@NoArgsConstructor
public class AdminController {
    @Autowired
    public TaskService taskService;
    @Autowired
    public UserService userService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public EmailService emailService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/main")
    public String getAllTasks(@RequestParam(name = "name", required = false) String name,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String search,
                              Model model) {
        List<User> users = userService.findAllUsers();
        List<Category> categories = categoryService.findAllCategories();
        page = page > 0 ? page - 1 : 0;

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        Page<Task> tasksPage;
       if( name != null && !name.isEmpty() && search != null && !search.isEmpty()){
           tasksPage = taskService.findTasksByUserNameContainingAndTitleContaining(name, search, pageable);
       }
       else if (name != null && !name.isEmpty()){
           tasksPage = taskService.findTasksByUserNameContaining(name, pageable);
       }
       else if (search != null && !search.isEmpty()){
           tasksPage = taskService.findTasksByTitleContaining(search, pageable);
       }
       else{
           tasksPage = taskService.findAllTasks(pageable);
       }

        // Добавляем данные в модель
        model.addAttribute("tasks", tasksPage.getContent());
        model.addAttribute("users", users != null ? users : new ArrayList<>());
        model.addAttribute("categories", categories != null ? categories : new ArrayList<>());
        model.addAttribute("usernameFilter", name);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksPage.getTotalPages());

        return "adminMain";
    }




    @GetMapping("/addCategory")
    public String showAddCategory(Model model){
        model.addAttribute("category", new Category());
        return "addCategory";
    }
    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category")@Validated Category category, Model model){
        categoryService.saveCategory(category);
        return "addCategory";

    }
    @PostMapping("/admin/sendNotification")
    public String sendNotification(@RequestParam("taskId") Long taskId) {
        Optional<Task> optionalTask = taskService.findById(taskId);
        optionalTask.ifPresent(task -> {
            if (task.getUser() != null && task.getUser().getEmail() != null) {
                String email = task.getUser().getEmail();
                String subject = "Task Notification: " + task.getTitle();
                String message = String.format(
                        "Hello %s,\n\nYou have a task assigned to you:\n\nTitle: %s\nDescription: %s\nDue Date: %s\n\nBest regards,\nAdmin",
                        task.getUser().getName(), task.getTitle(), task.getDescription(), task.getDueDate()
                );

                emailService.sendEmail(email, subject, message);
            }
        });

        return "redirect:/admin/main";
    }



}
