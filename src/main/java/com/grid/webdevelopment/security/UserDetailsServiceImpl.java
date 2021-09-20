package com.grid.webdevelopment.security;

import com.grid.webdevelopment.exception.UserNotFoundException;
import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User shopUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDetails securityUser = SecurityUser.fromUser(shopUser);
        return securityUser;
    }
}
