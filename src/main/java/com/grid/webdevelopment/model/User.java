package com.grid.webdevelopment.model;

import com.grid.webdevelopment.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty private String userId;
    @NotEmpty private String sessionId;
    @NotEmpty private String email;
    @NotEmpty private String password;
    private UserStatus userStatus;
    private Role role;
    private int failedAttempts;
    private long finishLocking;
    private Set<String> orders;

}
