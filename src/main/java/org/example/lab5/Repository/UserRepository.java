package org.example.lab5.Repository;

import org.example.lab5.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
public Optional<User> findByLogin(String login);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateUserName(Long id, String name);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    void updateUserEmail(Long id, String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updateUserPassword(Long id, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.image_url = :imageUrl WHERE u.id = :id")
    void updateUserImageUrl(Long id, String imageUrl);
}
