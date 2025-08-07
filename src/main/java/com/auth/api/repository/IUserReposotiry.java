package com.auth.api.repository;

import com.auth.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserReposotiry extends JpaRepository<User, UUID> {

	User findByNick(String nick);
}
