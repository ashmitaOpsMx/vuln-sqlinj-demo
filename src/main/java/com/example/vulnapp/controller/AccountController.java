package com.example.vulnapp.controller;

import com.example.vulnapp.model.Account;
import com.example.vulnapp.repo.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountRepository repo;

    public AccountController(AccountRepository repo) {
        this.repo = repo;
    }

    /**
     * Vulnerable endpoint:
     * GET /api/accounts?owner=Alice
     */
    @GetMapping("/accounts")
    public ResponseEntity<?> getAccountsByOwner(@RequestParam("owner") String owner) {
        try {
            List<Account> accounts = repo.findByOwnerUnsafe(owner);
            return ResponseEntity.ok(accounts);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(
                    "DB error: " + e.getMessage()
            );
        }
    }
}
