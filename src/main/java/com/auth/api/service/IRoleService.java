package com.auth.api.service;

import com.auth.api.dto.RoleDTO;
import com.auth.api.dto.RoleFilterDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {

	RoleDTO save(RoleDTO role);
	RoleDTO update(UUID id, RoleDTO role);
    void delete(UUID id);
    RoleDTO findByIdToDTO(UUID id);
    Page<RoleDTO> findAll(RoleFilterDTO userFilter, Pageable page);
    List<RoleDTO> getAll();
}
