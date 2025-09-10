package com.auth.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {

	private String nick;
	private List<UUID> uuids;
	private LocalDate created;
}
