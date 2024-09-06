package com.employee.management.app.Payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

	private List<DepartmentResponseDTO> data;
    private int currentPage;
    private int totalItems;
    private int totalPages;
}
