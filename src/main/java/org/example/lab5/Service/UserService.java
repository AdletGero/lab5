package org.example.lab5.Service;

import lombok.AllArgsConstructor;
import org.example.lab5.Entity.User;
import org.example.lab5.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}
    public void SaveUser(User user){
        user.setPassword(encoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
