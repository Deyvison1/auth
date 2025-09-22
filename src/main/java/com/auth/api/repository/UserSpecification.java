package com.auth.api.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.auth.api.dto.UserFilter;
import com.auth.api.models.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification {

	public static Specification<User> filterBy(UserFilter filter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (filter != null) {
				addNickFilter(filter, root, cb, predicates);
				addCreatedFilter(filter, root, cb, predicates);
				addRoleUuidFilter(filter, root, cb, predicates);
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	private static void addNickFilter(UserFilter filter, Root<User> root,
			CriteriaBuilder cb, List<Predicate> predicates) {
		if (filter.getNick() != null && !filter.getNick().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("nick")), "%" + filter.getNick().toLowerCase() + "%"));
		}
	}

	private static void addCreatedFilter(UserFilter filter, jakarta.persistence.criteria.Root<User> root,
			CriteriaBuilder cb, List<Predicate> predicates) {
		if (filter.getCreated() != null) {
			LocalDateTime startOfDay = filter.getCreated().atStartOfDay();
			LocalDateTime endOfDay = filter.getCreated().atTime(LocalTime.MAX);

			predicates.add(cb.between(root.get("created"), startOfDay, endOfDay));
		}
	}

	private static void addRoleUuidFilter(UserFilter filter, Root<User> root,
			CriteriaBuilder cb, List<Predicate> predicates) {
		if (filter.getUuids() != null && !filter.getUuids().isEmpty()) {
			Join<Object, Object> rolesJoin = root.join("roles");
			predicates.add(rolesJoin.get("uuid").in(filter.getUuids()));
		}
	}
}
