package com.grid.webdevelopment.service;

import com.grid.webdevelopment.config.CryptPasswordEncoder;
import com.grid.webdevelopment.exception.UserNotFoundException;
import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private SessionRegistry sessionRegistry;
    private UserRepository userRepository;
    private CryptPasswordEncoder passwordEncoder;

    //todo fix
    public Map<String, String> create(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        String password = passwordEncoder.getPasswordEncoder().encode(accessRequest.getPassword());
        if (userRepository.userExists(email)) {
            throw new UsernameNotFoundException("User with such email exists already");
        }
        User user = User.builder().email(email).password(password).build();
        userRepository.save(user);
        return Collections.singletonMap("id", user.getUserId());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", email)));
    }

    public List<User> getUsers() {
        return userRepository.findAllUsers();
    }

    public String delete(String id) {
        userRepository.deleteById(id);
        return "Successfully deleted";
    }


}
