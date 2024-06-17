package org.donis.tms.controller;

import org.donis.tms.models.TransactionRequestDTO;
import org.donis.tms.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> processTransactionRequest(@RequestHeader("Authorization") String token,
                                                            @RequestBody TransactionRequestDTO transactionRequestDTO) {

        //Note: Usually the exception should be done at some interceptor level and be generic
        //for all endpoints and controllers

        try {
            return ResponseEntity.ok(transactionService.processTransactionRequest(token, transactionRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
