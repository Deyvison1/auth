package com.auth.api.mapper.base;

import java.util.List;
import java.util.Optional;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface IBaseMapper<E, D> {
	D toDto(E entity);

	E toEntity(D dto);

	@Mapping(target = "uuid", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	E toEntity(D dto, @MappingTarget E entity);

	List<D> toDto(List<E> entities);

	List<D> toDto(Iterable<E> entities);

	List<E> toEntity(List<D> dtos);

	// -----------------------
	// Métodos opcionais seguros
	// -----------------------

	default Optional<D> toDtoOptional(E entity) {
		return Optional.ofNullable(entity).map(this::toDto);
	}

	default Optional<E> toEntityOptional(D dto) {
		return Optional.ofNullable(dto).map(this::toEntity);
	}

	default Optional<E> toEntityOptional(D dto, E entity) {
		return Optional.ofNullable(dto).map(d -> this.toEntity(d, entity));
	}
}
