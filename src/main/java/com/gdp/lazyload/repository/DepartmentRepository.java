package com.gdp.lazyload.repository;

import com.gdp.lazyload.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @EntityGraph(attributePaths = {"employees"})
    Optional<Department> findWithEmployeesNoLocationByDepartmentId(Integer deptId);

    @EntityGraph(attributePaths = {"employees", "location", "location.country", "location.country.region"})
    Optional<Department> findWithEmployeesAndLocationByDepartmentId(Integer deptId);

    @EntityGraph(attributePaths = {"location", "location.country", "location.country.region"})
    Optional<Department> findWithLocationByDepartmentId(Integer deptId);
}
