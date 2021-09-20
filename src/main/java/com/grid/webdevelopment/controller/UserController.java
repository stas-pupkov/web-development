package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> create(@RequestBody @Valid AccessRequest accessRequest) {
        return ResponseEntity.ok(userService.createUser(accessRequest));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
