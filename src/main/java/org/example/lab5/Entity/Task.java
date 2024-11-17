package org.example.lab5.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Task")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(length = 20)
    private String status = "Pending";

    private Integer priority = 1;

    // Связь с User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Связь с Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



}
