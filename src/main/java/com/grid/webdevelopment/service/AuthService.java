package com.grid.webdevelopment.service;

import com.grid.webdevelopment.config.CryptPasswordEncoder;
import com.grid.webdevelopment.exception.AuthenticationException;
import com.grid.webdevelopment.exception.MaxAttemptsException;
import com.grid.webdevelopment.exception.UserLockedException;
import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.SessionResponse;
import com.grid.webdevelopment.model.Status;
import com.grid.webdevelopment.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final int LOCK_TIME_SEC = 30;

    private final UserService userService;
    private final CryptPasswordEncoder passwordEncoder;
    private final SessionRegistry sessionRegistry;

    public SessionResponse loginUser(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();

        User user = userService.getUserByEmail(email);
        checkLocked(user);
        checkFailedAttempts(user);

        String rawPassword = accessRequest.getPassword();
        String encodedPassword = userService.getUserByEmail(accessRequest.getEmail()).getPassword();

        boolean passwordsEqual = comparePasswords(rawPassword, encodedPassword);

        if (passwordsEqual) {
            user.setStatus(Status.ACTIVE);
            user.setFailedAttempts(0);
            user.setFinishLocking(0);

            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            user.setSessionId(sessionId);
            log.info("User {} has got sessionId={}", email, sessionId);
            return SessionResponse.builder().sessionId(sessionId).build();
        } else {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            throw new AuthenticationException(String.format("Wrong password for user %s", email));
        }
    }

    public User resetPassword(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        User user = userService.getUserByEmail(email);
        String newPassword = passwordEncoder.getPasswordEncoder().encode(accessRequest.getPassword());

        expireUserSessions(email);

        user.setPassword(newPassword);
        userService.saveUser(user);
        return user;
    }

    protected boolean comparePasswords(String rawPassword, String encodedPassword) {
        return passwordEncoder.getPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    protected void checkLocked(User user) {
        long currentTime = Instant.now().getEpochSecond();
        if (user.getStatus().equals(Status.LOCKED)) {
            if (user.getFinishLocking() > currentTime) {
                throw new UserLockedException(String.format("User %s is still locked", user.getEmail()));
            } else {
                user.setFailedAttempts(0);
            }
        }
    }

    protected void checkFailedAttempts(User user) {
        long timeToUnlock = Instant.now().getEpochSecond() + LOCK_TIME_SEC;
        if (user.getFailedAttempts() == MAX_FAILED_ATTEMPTS) {
            user.setStatus(Status.LOCKED);
            user.setFinishLocking(timeToUnlock);
            throw new MaxAttemptsException(String.format("User %s used all attempts to login", user.getEmail()));
        }
    }

    public void expireUserSessions(String username) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof org.springframework.security.core.userdetails.User)
                .map(userDetails -> (UserDetails) userDetails)
                .filter(userDetails -> userDetails.getUsername().equals(username))
                .map(userDetails -> (SessionInformation) sessionRegistry.getAllSessions(userDetails, true))
                .forEach(information -> information.expireNow());
    }



}
