package com.auth.api.service.impl;

import com.auth.api.dto.RoleDTO;
import com.auth.api.exception.NotChangedException;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.mapper.IRoleMapper;
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
    private final IRoleMapper roleMapper;

    @Override
    public RoleDTO save(RoleDTO role) {
    	Role entity = roleMapper.toEntity(role);
        return roleMapper.toDto(repository.save(entity));
    }

    @Override
    public RoleDTO update(UUID id, RoleDTO role) {
        Role roleSaved = findById(id);
        if(!compareValuesChange(role, roleSaved)) throw new NotChangedException();
        Role roleSave = mouthRole(role);
        return roleMapper.toDto(repository.save(roleSave));
    }

    private boolean compareValuesChange(RoleDTO roleDTO, Role roleSaved) {
        return !roleDTO.getName().equals(roleSaved.getName()) || !roleDTO.getDescription().equals(roleSaved.getDescription());
    }

    private Role mouthRole(RoleDTO roleDTO) {
        Role roleSave =  new Role();
        roleSave.setName(roleDTO.getName());
        roleSave.setDescription(roleDTO.getDescription());
        roleSave.setUuid(roleDTO.getUuid());
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
