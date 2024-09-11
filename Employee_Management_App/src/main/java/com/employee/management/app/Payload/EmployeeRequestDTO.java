package com.employee.management.app.Payload;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {
	
	private int id;

	@NotNull(message = "Employee code is required")
    @Size(max = 15, message = "Employee code should not exceed 15 characters")
    @Column(name = "empCode", nullable = false, unique = true)
    private String empCode;

    @NotNull(message = "First name is required")
    @Size(max = 50, message = "First name should not exceed 50 characters")
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(max = 50, message = "Last name should not exceed 50 characters")
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @NotNull(message = "Full name is required")
    @Size(max = 100, message = "Full name should not exceed 100 characters")
    @Column(name = "fullName", nullable = false)
    private String fullName;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number should be exactly 10 digits")
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @NotNull(message = "Date of Joining (DOJ) is required")
    private LocalDate doj;

    @NotNull(message = "Organization ID is required")
    private int organizationId;

    @NotNull(message = "Designation ID is required")
    private int designationId;
   
}
