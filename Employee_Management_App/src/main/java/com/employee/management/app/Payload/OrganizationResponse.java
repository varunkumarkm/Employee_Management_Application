package com.employee.management.app.Payload;

import java.util.List;
import lombok.Data;

@Data
public class OrganizationResponse {
    private List<OrganizationResponse> data;

    public OrganizationResponse(List<OrganizationResponse> data) {
        this.data = data;
    }

    public List<OrganizationResponse> getData() {
        return data;
    }

    public void setData(List<OrganizationResponse> data) {
        this.data = data;
    }
}
