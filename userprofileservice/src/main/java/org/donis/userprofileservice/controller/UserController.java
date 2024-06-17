package org.donis.userprofileservice.controller;

import org.donis.models.dto.UserDTO;
import org.donis.models.entities.User;
import org.donis.models.mappers.UserMapper;
import org.donis.userprofileservice.services.UserService;
import org.donis.utilities.JwtHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user", produces = "application/json")
    public ResponseEntity<UserDTO> retrieveUserInfo(@RequestHeader("Authorization") String token) {
        String username = JwtHelper.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(userService.findUser(username));
    }

    @PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/user-details")
    public ResponseEntity<User> retrieveUserDetails(@RequestHeader("Authorization") String token) {
        String username = JwtHelper.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(userService.findUserDetails(username));
    }
}
