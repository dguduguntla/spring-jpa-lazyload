package com.gdp.lazyload.service;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.domain.Departments;
import com.gdp.lazyload.domain.LocationDTO;
import com.gdp.lazyload.entity.Department;
import com.gdp.lazyload.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private ModelMapper modelMapper;
    private DepartmentRepository deptRepo;

    public DepartmentServiceImpl(ModelMapper modelMapper, DepartmentRepository deptRepo) {
        this.modelMapper = modelMapper;
        this.deptRepo = deptRepo;
    }

    @Override
    public Departments getAllDepartments() {
        List<Department> departmentEntities = deptRepo.findAll();
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        for (Department dept : departmentEntities) {
            DepartmentDTO departmentDTO = modelMapper.map(dept, DepartmentDTO.class);
            departmentDTOS.add(departmentDTO);
        }
        Departments departments = new Departments();
        departments.setDepartments(departmentDTOS);
        return departments;
    }

    @Override
    public DepartmentDTO getDepartment(Integer departmentId, boolean includeEmployees, boolean includeLocation) {
        if (includeEmployees && !includeLocation) {
            return deptRepo.findWithEmployeesNoLocationByDepartmentId(departmentId)
                    .map(dept -> modelMapper.map(dept, DepartmentDTO.class))
                    .orElseThrow(resourceNotFound(departmentId));
        } else if (includeLocation) {
            Optional<Department> deptEntity;
            if (includeEmployees) {
                //both location and employees
                deptEntity = deptRepo.findWithEmployeesAndLocationByDepartmentId(departmentId);
            } else {
                //only location.
                deptEntity = deptRepo.findWithLocationByDepartmentId(departmentId);
            }
            return deptEntity
                    .map(dept -> {
                        DepartmentDTO deptDto = modelMapper.map(dept, DepartmentDTO.class);
                        //explicitly add location.
                        deptDto.setLocation(modelMapper.map(dept.getLocation(), LocationDTO.class));
                        return deptDto;
                    })
                    .orElseThrow(resourceNotFound(departmentId));
        } else {
            //both flags are false, so no association fields (employees, location) to be fetched.
            return deptRepo.findById(departmentId)
                    .map(dept -> modelMapper.map(dept, DepartmentDTO.class))
                    .orElseThrow(resourceNotFound(departmentId));
        }
    }

    private Supplier<IllegalArgumentException> resourceNotFound(Integer departmentId) {
        return () ->
                new IllegalArgumentException(String.format("The DepartmentId: %d is not found!", departmentId));
    }
}
