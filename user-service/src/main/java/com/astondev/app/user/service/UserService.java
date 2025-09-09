package com.astondev.app.user.service;

import com.astondev.app.user.model.User;
import com.astondev.app.user.repository.UserRepository;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        logger.info("Creating user {}", user);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Deleted user {}", id);
            return true;
        }
        return false;
    }

    public User updateUser(Long id, User updareUser) {
        return userRepository.findById(id)
                .map(user -> {
                  user.setName(updareUser.getName());
                  user.setEmail(updareUser.getEmail());
                  user.setAge(updareUser.getAge());
                  return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }
}
