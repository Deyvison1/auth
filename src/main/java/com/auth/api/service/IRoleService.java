package com.auth.api.service;

import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.models.Role;

import java.util.UUID;

public interface IRoleService {

    Role save(Role role);
    Role update(UUID id, Role role);
    void delete(UUID id);
}
