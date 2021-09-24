package com.grid.webdevelopment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.constraints.NotEmpty;
import java.util.Map;
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
    private Status status;
    private Role role;
    private int failedAttempts;
    private long finishLocking;
    private Set<String> orders;

}
