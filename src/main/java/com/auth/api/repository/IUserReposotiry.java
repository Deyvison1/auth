package com.auth.api.repository;

import com.auth.api.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface IUserReposotiry extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

	User findByNick(String nick);
	Page<User> findByNickOrRolesIn(Pageable page, String nick, List<UUID> roles);

}
