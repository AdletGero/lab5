package org.example.lab5.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    public List<Task> findTasksByUser(Long id){
        return taskRepository.findByUserId(id);
    }
    public void saveTask(Task task){
        taskRepository.save(task);
    }
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    public List<Task> findTasksByUserSortedById(Long userId) {
        return taskRepository.findByUserIdOrderByIdAsc(userId); // Сортировка по ID
    }
    public Page<Task> findAllTasks(Pageable pageable ){
        return (Page<Task>) taskRepository.findAll(pageable);
    }

    public Page<Task> findTasksByUserNameContaining(String name, Pageable pageable) {
        return taskRepository.findByUserNameContainingIgnoreCase(name, pageable);
    }

    public Page<Task> findTasksByTitleContaining(String title, Pageable pageable) {
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    public Page<Task> findTasksByUserNameContainingAndTitleContaining(String name, String title, Pageable pageable) {
        return taskRepository.findTasksByUserNameContainingAndTitleContainingIgnoreCase(name, title, pageable);
    }
    public Page<Task> findTasksWithPaginationAndSearch(String status, Long categoryId, Long userId, String search, Pageable pageable) {
        return taskRepository.findTasksByFilterWithSearch(status, categoryId, userId, search, pageable);
    }




}
