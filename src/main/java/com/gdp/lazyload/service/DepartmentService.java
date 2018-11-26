package com.gdp.lazyload.service;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.domain.Departments;

public interface DepartmentService {

    Departments getAllDepartments();

    DepartmentDTO getDepartment(Integer departmentId, boolean includeEmployees, boolean includeLocation);
}
