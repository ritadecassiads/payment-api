package com.payment.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.api.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor	
public class EventResponseDTO {
	private Account origin;
	private Account destination;
}
