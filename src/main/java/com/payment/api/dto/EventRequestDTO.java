package com.payment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
	private String type;
	private String origin;
	private String destination;
	private Integer amount;
}
