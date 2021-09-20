package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRepository {

    private final DefaultUsers defaultUsers;

    public Optional<User> findByEmail(String email) {
        return defaultUsers.getUsers().values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public void save(User user) {
        defaultUsers.getUsers().put(user.getUserId(), user);
    }

    public List<User> findAll() {
        return defaultUsers.getUsers().values().stream().collect(Collectors.toList());
    }

    public void deleteById(String id) {
        defaultUsers.getUsers().keySet().removeIf(userId -> userId.equals(id));
    }

    public boolean userExists(String email) {
        return defaultUsers.getUsers().values().stream().filter(user -> user.getEmail().equals(email)).count() == 1;
    }

}
