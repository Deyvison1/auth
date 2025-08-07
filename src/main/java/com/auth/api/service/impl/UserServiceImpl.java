package com.auth.api.service.impl;

import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final IUserReposotiry repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(UUID id, User user) throws NotFoundEntityException {
    	User updatedUser = findById(id);
        if(thereHaveBeenChanges(updatedUser, user)) {
            return repository.save(monthEntity(updatedUser, user));
        }
        return updatedUser;
    }


    public User findByNick(String nick) {
        return repository.findByNick(nick);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    private User monthEntity(User userSave, User userRequest) {
        userSave.setNick(userRequest.getNick());
        userSave.setEmail(userRequest.getEmail());
        return userSave;
    }


    public User findById(UUID id) throws NotFoundEntityException {
        return repository.findById(id).orElseThrow(NotFoundEntityException::new);
    }


    private Boolean thereHaveBeenChanges(User userSave, User userRequest) {
        if(!Objects.equals(userSave.getNick(), userRequest.getNick()) || !Objects.equals(userSave.getPassword(), userRequest.getPassword()) || !Objects.equals(userSave.getEmail(), userRequest.getEmail())) {
            return true;
        }
        return false;
    }

	@Override
	public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
		User user = findByNick(nick);
		return user;
	}
}
