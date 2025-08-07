package com.auth.api.controller;

import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.models.User;
import com.auth.api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(service.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) throws NotFoundEntityException {
        return ResponseEntity.ok(service.update(id, user));
    }
}