package com.grid.webdevelopment.security;

import com.grid.webdevelopment.model.User;
import com.grid.webdevelopment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User shopUser = userService.getUserByEmail(email);
        UserDetails securityUser = SecurityUser.fromUser(shopUser);
        return securityUser;
    }
}
