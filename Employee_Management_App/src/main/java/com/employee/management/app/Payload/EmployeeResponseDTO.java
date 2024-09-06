package com.employee.management.app.Payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {

	private int total;
    private int page;
    private int pageSize;
    private List<EmployeeDTO> data;
}
