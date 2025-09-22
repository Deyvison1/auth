package com.auth.api.repository;

import com.auth.api.dto.LabelValueDTO;
import com.auth.api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
	@Query("SELECT new com.auth.api.dto.LabelValueDTO(r.name, r.uuid) FROM Role r")
	List<LabelValueDTO> findAllLabelValue();
}
