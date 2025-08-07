package com.auth.api.repository;

import com.auth.api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
}
