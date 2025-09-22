package com.auth.api.service.impl;

import com.auth.api.dto.RoleDTO;
import com.auth.api.dto.RoleFilterDTO;
import com.auth.api.dto.UserDTO;
import com.auth.api.exception.NotChangedException;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.mapper.IRoleMapper;
import com.auth.api.models.Role;

import org.springframework.data.domain.Page;
import com.auth.api.repository.IRoleRepository;
import com.auth.api.repository.RoleSpecification;
import com.auth.api.service.IAuthService;
import com.auth.api.service.IRoleService;
import com.auth.api.service.IUserService;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository repository;
    private final IRoleMapper roleMapper;
    private final IAuthService authService;
    private final IUserService userService;

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
    	DecodedJWT decodedToken = this.authService.getDecodedToken();
    	UserDTO user = userService.findByNick(decodedToken.getSubject());
        Role roleSave =  new Role();
        roleSave.setName(roleDTO.getName());
        roleSave.setDescription(roleDTO.getDescription());
        roleSave.setUuid(roleDTO.getUuid());
        roleSave.setUserUpdateId(user.getUuid());
        return roleSave;
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
    
    private Role findById(UUID id) {
    	 return repository.findById(id).orElseThrow(NotFoundEntityException::new);
    }

    @Override
    public RoleDTO findByIdToDTO(UUID id)  {
    	Role role = findById(id);
    	return roleMapper.toDto(role);
    }

	@Override
	public Page<RoleDTO> findAll(RoleFilterDTO roleFilter, Pageable page) {
		Specification<Role> spec = RoleSpecification.filterBy(roleFilter);
		Page<Role> entities = repository.findAll(spec, page);
		List<RoleDTO> dtos = roleMapper.toDto(entities.getContent());
		return new PageImpl<RoleDTO>(dtos, page, entities.getTotalElements());
	}

	@Override
	public List<RoleDTO> getAll() {
		return roleMapper.toDto(repository.findAll());
	}
	
}
