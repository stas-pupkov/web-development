package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.config.CryptPasswordEncoder;
import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.SessionResponse;
import com.grid.webdevelopment.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CryptPasswordEncoder passwordEncoder;

    @PostMapping("login")
    public SessionResponse login(@RequestBody AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        String rawPassword = accessRequest.getPassword();
        String encodedPassword = userService.getUserByEmail(accessRequest.getEmail()).getPassword();
        boolean coincidence = passwordEncoder.getPasswordEncoder().matches(rawPassword, encodedPassword);

        if (coincidence) {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            userService.getUserByEmail(email).setSessionId(sessionId);
            log.info("User {} has got sessionId={}", email, sessionId);
            return SessionResponse.builder().sessionId(sessionId).build();
        } else {
            throw new UsernameNotFoundException("Not found 3");
        }
    }

    @PostMapping("logout")
    @PreAuthorize("hasAuthority('users:read')")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
    }

}
