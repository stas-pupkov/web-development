package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.AuthenticationResponse;
import com.grid.webdevelopment.model.Permission;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.service.AuthenticationService;
import com.grid.webdevelopment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody @Valid AccessRequest accessRequest) {
        return ResponseEntity.ok(userService.create(accessRequest));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @PostMapping("auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AccessRequest accessRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(accessRequest));
    }

    @GetMapping("users")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<String>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

}
