package com.example.tbd_lab2.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteGastoResponse {

	private Long id;
	private String username;
	private String email;
	private Integer totalGastado;
}
