package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    private String sessionId;

}
