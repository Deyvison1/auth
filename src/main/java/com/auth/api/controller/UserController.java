package com.auth.api.controller;

import com.auth.api.dto.UserDTO;
import com.auth.api.exception.NotFoundEntityException;
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

    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) {
        return ResponseEntity.ok(service.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @RequestBody UserDTO user) {
        return ResponseEntity.ok(service.update(id, user));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-login/{nick}")
    public ResponseEntity<UserDTO> findByNick(@PathVariable String nick) {
        return ResponseEntity.ok(service.findByNick(nick));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-user-id/{nick}")
    public ResponseEntity<UUID> findUUIDByNick(@PathVariable String nick) {
        return ResponseEntity.ok(service.findUUidByNick(nick));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.findByIdToDTO(uuid));
    }

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")	
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
}