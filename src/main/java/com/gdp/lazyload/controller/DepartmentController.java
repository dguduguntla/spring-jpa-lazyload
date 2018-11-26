package com.gdp.lazyload.controller;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.domain.Departments;
import com.gdp.lazyload.repository.DepartmentRepository;
import com.gdp.lazyload.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService, DepartmentRepository repository, ModelMapper modelMapper) {
        this.departmentService = departmentService;
    }

    @GetMapping("/department")
    public ResponseEntity<Departments> getDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable Integer departmentId, @RequestParam(required = false, defaultValue = "false") boolean includeEmployees, @RequestParam(required = false, defaultValue = "false") boolean includeLocation) {
        return ResponseEntity.ok(departmentService.getDepartment(departmentId, includeEmployees, includeLocation));
    }

}
