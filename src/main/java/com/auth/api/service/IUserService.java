package com.auth.api.service;

import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.models.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {

	User save(User user);
	User update(UUID id, User user) throws NotFoundEntityException;
	User findByNick(String nick);
    List<User> findAll();
}
