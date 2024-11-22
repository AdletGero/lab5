package org.example.lab5.Repository;

import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long user_id);
    @Query("SELECT t FROM Task t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(:userId IS NULL OR t.user.id = :userId) AND " +
            "(:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findTasksByFilterWithSearch(@Param("status") String status,
                                           @Param("categoryId") Long categoryId,
                                           @Param("userId") Long userId,
                                           @Param("search") String search,
                                           Pageable pageable);
    public List<Task> findByUserIdOrderByIdAsc(Long userId);
    Page<Task> findByUserNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Task> findTasksByUserNameContainingAndTitleContainingIgnoreCase(String name, String title, Pageable pageable);


}
