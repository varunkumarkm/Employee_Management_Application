package com.employee.management.app.Payload;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PastExperienceResponseDTO {

	private int id;

    @NotNull(message = "Employee ID is required")
    private int employeeId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Designation is required")
    private DesignationResponseDTO  designation;   

    private String responsibilities;
}
