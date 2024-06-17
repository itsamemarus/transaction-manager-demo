package org.donis.userprofileservice.controller;


import org.apache.catalina.connector.Response;
import org.donis.models.dto.AccountDTO;
import org.donis.userprofileservice.services.AccountService;
import org.donis.utilities.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // CREATE ACCOUNT ENDPOINT
    @PostMapping("/account")
    public ResponseEntity<AccountDTO> createAccount(@RequestHeader("Authorization") String token, @RequestBody AccountDTO accountDTO){
        String username = JwtHelper.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(accountService.createAccount(username, accountDTO));
    }
}
