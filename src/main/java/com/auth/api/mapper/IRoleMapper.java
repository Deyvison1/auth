package com.auth.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.auth.api.dto.RoleDTO;
import com.auth.api.mapper.base.IBaseMapper;
import com.auth.api.models.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleMapper extends IBaseMapper<Role, RoleDTO> {

}
