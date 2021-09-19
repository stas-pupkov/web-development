package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.exception.UserNotExistsException;
import com.grid.webdevelopment.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserRepository {

    private Map<String, User> users = Stream.of(
            new User("1", "admin2", "$2a$12$yrlAxAx/MPI9zIF5ErnEPe0sh7757KqC5waR5otGCdkCDnO/Ec3L6"),
            new User("2", "user2", "$2a$12$bgFDAp.mnmpW/cLfvuWpg.Me0Oj2npvUppMwMpd8jbSgUOSJmXHIe"),
            new User("3", "email3", "3"))
            .collect(Collectors.toMap(User::getId, Function.identity()));

    public User get(String id) {
        return users.get(id);
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public List<String> getAll() {
        return users.keySet().stream().collect(Collectors.toList());
    }

    public void delete(String id) {
        users.keySet().removeIf(userId -> userId.equals(id));
    }

    public boolean userExists(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).count() == 1;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

}
