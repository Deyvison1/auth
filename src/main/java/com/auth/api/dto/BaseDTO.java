package com.auth.api.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDTO {
	private UUID uuid;
}
