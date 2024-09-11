package com.employee.management.app.Payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {

	private int id;
	
	@NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private Integer organizationId;
}
