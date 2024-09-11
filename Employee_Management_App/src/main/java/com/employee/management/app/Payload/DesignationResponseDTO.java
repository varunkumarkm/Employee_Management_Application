package com.employee.management.app.Payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationResponseDTO {

	private int id;
	
	@NotNull(message = "name is required")
    @NotEmpty(message = "name cannot be empty")
    private String name;
}
