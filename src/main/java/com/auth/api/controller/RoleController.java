package com.auth.api.controller;

import com.auth.api.dto.RoleDTO;
import com.auth.api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

	private final IRoleService service;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<RoleDTO> save(@RequestBody RoleDTO role) {
		return ResponseEntity.ok().body(service.save(role));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<RoleDTO> update(@PathVariable UUID id, @RequestBody RoleDTO role) {
		return ResponseEntity.ok().body(service.update(id, role));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
}