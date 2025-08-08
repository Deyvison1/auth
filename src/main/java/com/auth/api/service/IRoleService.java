package com.auth.api.service;

import com.auth.api.dto.RoleDTO;
import java.util.UUID;

public interface IRoleService {

	RoleDTO save(RoleDTO role);
	RoleDTO update(UUID id, RoleDTO role);
    void delete(UUID id);
}
