package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.AuthenticationException;
import com.grid.webdevelopment.model.AccessRequest;
import com.grid.webdevelopment.model.AuthenticationResponse;
import com.grid.webdevelopment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private UserRepository userRepository;

    public AuthenticationResponse authenticate(AccessRequest accessRequest) {
        String email = accessRequest.getEmail();
        String password = String.valueOf(accessRequest.getPassword().hashCode());
        String authString = email + ":" + password;
        byte[] authStringEnc = Base64.getEncoder().encode(authString.getBytes());
        if (isUserAuthenticated(authStringEnc)) {
            String sessionId = UUID.randomUUID().toString();
            return AuthenticationResponse.builder().sessionId(sessionId).build();
        } else {
            throw new AuthenticationException("User is not authorized");
        }
    }

    private boolean isUserAuthenticated(byte[] authStringEnc){

        byte[] bytes = Base64.getDecoder().decode(authStringEnc);
        String decodedAuth = new String(bytes);
        System.out.println(decodedAuth);
        String email = decodedAuth.substring(0, decodedAuth.indexOf(":"));
        if (userRepository.get(email) != null) return true;
        return false;
    }


}
