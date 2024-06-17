package org.donis.userprofileservice.controller;

import org.donis.models.dto.LoginRequest;
import org.donis.models.dto.UserDTO;
import org.donis.models.entities.User;
import org.donis.models.mappers.UserMapper;
import org.donis.userprofileservice.services.UserService;
import org.donis.utilities.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String token = JwtHelper.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok(token);
    }

}
