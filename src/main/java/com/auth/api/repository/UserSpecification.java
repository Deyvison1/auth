package com.auth.api.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.auth.api.dto.UserFilter;
import com.auth.api.models.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
	public static Specification<User> filterBy(UserFilter filter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if(filter != null) {
				if (filter.getNick() != null && !filter.getNick().isEmpty()) {
					predicates.add(cb.like(cb.lower(root.get("nick")), "%" + filter.getNick().toLowerCase() + "%"));
				}
				
				if (filter.getCreated() != null) {
				    LocalDateTime startOfDay = filter.getCreated().atStartOfDay(); 
				    LocalDateTime endOfDay = filter.getCreated().atTime(LocalTime.MAX);

				    predicates.add(cb.between(root.get("created"), startOfDay, endOfDay));
				}


				if (filter.getUuids() != null && !filter.getUuids().isEmpty()) {
					Join<Object, Object> rolesJoin = root.join("roles");
					predicates.add(rolesJoin.get("uuid").in(filter.getUuids()));
				}
			}
			

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

}
