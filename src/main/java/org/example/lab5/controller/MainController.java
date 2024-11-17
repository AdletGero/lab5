package org.example.lab5.controller;

import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.TaskRepository;
import org.example.lab5.Service.CategoryService;
import org.example.lab5.Service.TaskService;
import org.example.lab5.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public String main(@RequestParam(value = "status", required = false, defaultValue = "") String status,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();

        String statusFilter = status.equals("All") || status.isEmpty() ? null : status;
        List<Task> tasks;
        if (status == "all" && categoryId == null) {
            tasks = taskService.findTasksByUserSortedById(userId);
        } else {
            tasks = taskRepository.findTasksByFilter(statusFilter, categoryId, userId);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("status", "all");
        model.addAttribute("category", null);
        return "main";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/main")
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllTasks());
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
}
