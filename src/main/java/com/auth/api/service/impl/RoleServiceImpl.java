package com.auth.api.service.impl;

import com.auth.api.exception.NotChangedException;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.models.Role;
import com.auth.api.repository.IRoleRepository;
import com.auth.api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository repository;

    @Override
    public Role save(Role role) {
        return repository.save(role);
    }

    @Override
    public Role update(UUID id, Role role) {
        Role roleSaved = findById(id);
        if(!compareValuesChange(role, roleSaved)) throw new NotChangedException();
        Role roleSave = mouthRole(role);
        return repository.save(roleSave);
    }

    private boolean compareValuesChange(Role role, Role roleSaved) {
        return !role.getName().equals(roleSaved.getName()) || !role.getDescription().equals(roleSaved.getDescription());
    }

    private Role mouthRole(Role role) {
        Role roleSave =  new Role();
        roleSave.setName(role.getName());
        roleSave.setDescription(role.getDescription());
        roleSave.setUuid(role.getUuid());
        return roleSave;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
    }

    private Role findById(UUID id)  {
        return repository.findById(id).orElseThrow(NotFoundEntityException::new);
    }
}
