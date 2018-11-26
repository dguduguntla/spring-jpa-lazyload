package com.gdp.lazyload.domain;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;
}
