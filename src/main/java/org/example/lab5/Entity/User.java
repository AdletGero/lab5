package org.example.lab5.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "date", nullable = false)
    private LocalDate date = LocalDate.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    private ERole role = ERole.ROLE_USER;

    @Column(nullable = false, length = 255)
    private String email;

    private String image_url;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now();
    }



}
