package org.example.lab5.controller;

import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.TaskRepository;
import org.example.lab5.Service.CategoryService;
import org.example.lab5.Service.TaskService;
import org.example.lab5.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                       @RequestParam(value = "search", required = false, defaultValue = "") String search,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();

        // Устанавливаем фильтры
        String statusFilter = status.equalsIgnoreCase("all") || status.isEmpty() ? null : status;

        // Создаем пагинацию
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        Page<Task> tasksPage = taskService.findTasksWithPaginationAndSearch(statusFilter, categoryId, userId, search, pageable);

        // Добавляем атрибуты в модель
        model.addAttribute("tasks", tasksPage.getContent());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("status", status);
        model.addAttribute("category", categoryId);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tasksPage.getTotalPages());
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
