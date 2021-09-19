package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.SessionResponse;
import com.grid.webdevelopment.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<SessionResponse> login(@RequestBody AccessRequest accessRequest) {
        return ResponseEntity.ok(authService.loginUser(accessRequest));
    }

    @PostMapping("logout")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
        return ResponseEntity.ok().build();
    }

}
