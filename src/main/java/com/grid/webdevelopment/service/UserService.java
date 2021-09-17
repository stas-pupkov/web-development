package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.UserExistsException;
import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public Map<String, String> create(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        String password = String.valueOf(accessRequest.getPassword().hashCode());
        if (userRepository.userExists(email)) {
            throw new UserExistsException("User with such email exists already");
        }
        User user = new User(email, password);
        userRepository.save(user);
        return Collections.singletonMap("id", user.getId());
    }

    public List<String> getUsers() {
        return userRepository.getAll();
    }

    public String delete(String id) {
        userRepository.delete(id);
        return "Successfully deleted";
    }

}
