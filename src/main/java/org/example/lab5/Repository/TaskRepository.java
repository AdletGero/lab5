package org.example.lab5.Repository;

import org.example.lab5.Entity.Task;
import org.example.lab5.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long user_id);
    @Query("SELECT t FROM Task t WHERE (:status IS NULL OR t.status = :status) " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
            "AND t.user.id = :userId ORDER BY t.dueDate ASC")
    List<Task> findTasksByFilter(@Param("status") String status,
                                 @Param("categoryId") Long categoryId,
                                 @Param("userId") Long userId);
    public List<Task> findByUserIdOrderByIdAsc(Long userId);

}
