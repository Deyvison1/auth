package com.auth.api.controller;

import com.auth.api.dto.RoleDTO;
import com.auth.api.dto.RoleFilterDTO;
import com.auth.api.service.IRoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<RoleDTO>> findAll(@ModelAttribute RoleFilterDTO userFilter, Pageable page) {
		return ResponseEntity.ok().body(service.findAll(userFilter, page));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-all")
	public ResponseEntity<List<RoleDTO>> findAll() {
		return ResponseEntity.ok().body(service.getAll());
	}
}