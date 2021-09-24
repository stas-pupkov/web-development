package com.grid.webdevelopment.service;

import com.grid.webdevelopment.config.CryptPasswordEncoder;
import com.grid.webdevelopment.exception.UserExistsException;
import com.grid.webdevelopment.exception.UserNotFoundException;
import com.grid.webdevelopment.model.*;
import com.grid.webdevelopment.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private CryptPasswordEncoder passwordEncoder;

    public User createUser(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        if (userRepository.userExists(email)) {
            throw new UserExistsException(String.format("User with email=%s exists already", email));
        }

        String encodePassword = passwordEncoder.getPasswordEncoder().encode(accessRequest.getPassword());
        User user = createNewUser(email, encodePassword);
        userRepository.save(user);

        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email=%s not found", email)));
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s not found", id)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
        log.info("User with id={} has been deleted", id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
        log.info("User with id={} has been saved/updated", user.getUserId());
    }

    protected User createNewUser(String email, String password) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .email(email)
                .password(password)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .failedAttempts(0)
                .finishLocking(0)
                .cart(new Cart())
                .build();
    }


}
