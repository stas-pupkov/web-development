package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.security.Role;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.model.UserStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Data
public class DefaultUsers {

    private String adminEmail;
    private String adminPassword;
    private Map<String, User> users = new HashMap<>();

    public DefaultUsers(@Value("${user.admin.email}") String adminEmail,
                        @Value("${user.admin.password}") String adminPassword) {
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @PostConstruct
    private void init() {
        users = Stream.of(createDefaultAdmin()).collect(Collectors.toMap(User::getUserId, Function.identity()));
    }

    private User createDefaultAdmin() {
        return User.builder()
                .userId("1")
                .email(adminEmail)
                .password(adminPassword)
                .role(Role.ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .orders(new HashSet<>())
                .build();
    }


}
