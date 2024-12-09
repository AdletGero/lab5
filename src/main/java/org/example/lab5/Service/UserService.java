package org.example.lab5.Service;

import lombok.AllArgsConstructor;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    private BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public String encodePassword(String password) {
        return encoder().encode(password);
    }

    public void SaveUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUserEmail(Long userId, String email) {
        userRepository.updateUserEmail(userId,email);
    }

    public void updateUserPassword(Long userId, String encodedPassword) {
        userRepository.updateUserPassword(userId, encodedPassword);
    }

    public void updateUserName(Long id, String name) {
        userRepository.updateUserName(id, name);
    }

    public void updateUserImageUrl(Long id, String url) {
        userRepository.updateUserImageUrl(id, url);
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
