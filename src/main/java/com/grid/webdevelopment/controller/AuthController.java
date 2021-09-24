package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.SessionResponse;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.service.AuthService;
import com.grid.webdevelopment.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CartService cartService;

    @PostMapping("login")
    public ResponseEntity<SessionResponse> login(@RequestBody AccessRequest accessRequest) {
        return ResponseEntity.ok(authService.loginUser(accessRequest));
    }

    @PostMapping("logout/{userId}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<?> logout(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) {
        cartService.returnAllProductsToShop(userId);
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset")
    public ResponseEntity<User> reset(@RequestBody AccessRequest accessRequest) {
        return ResponseEntity.ok(authService.resetPassword(accessRequest));
    }

}
