package com.gdp.lazyload;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.domain.Departments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAllDepartments_ShouldNotLoadEmployeesLocation() {
        ResponseEntity<Departments> departmentsResponse = testRestTemplate.getForEntity("/department", Departments.class);
        assertThat(departmentsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Departments departments = departmentsResponse.getBody();
        assertThat(departments.getDepartments()).hasSize(6);
        assertThat(departments.getDepartments()).extracting("employees").containsNull();
        assertThat(departments.getDepartments()).extracting("location").containsNull();
    }

    @Test
    public void getSingleDeparment_ShouldNotLoadEmployeesLocation() {
        ResponseEntity<DepartmentDTO> departmentResponse = testRestTemplate.getForEntity("/department/{departmentId}", DepartmentDTO.class, "4");
        assertThat(departmentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepartmentDTO dept = departmentResponse.getBody();
        assertThat(dept.getDepartmentName()).isNotBlank();
        assertThat(dept.getEmployees()).isNull();
        assertThat(dept.getLocation()).isNull();
    }

    @Test
    public void getSingleDeparment_WithIncludeEmployees_ShouldLoadEmployees() {
        ResponseEntity<DepartmentDTO> departmentResponse = testRestTemplate.getForEntity("/department/{departmentId}?includeEmployees=true", DepartmentDTO.class, "4");
        assertThat(departmentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepartmentDTO dept = departmentResponse.getBody();
        assertThat(dept.getDepartmentName()).isNotBlank();
        assertThat(dept.getEmployees()).isNotNull().hasSize(2);
        assertThat(dept.getLocation()).isNull();
    }

    @Test
    public void getSingleDeparment_WithIncludeEmployeesAndLocation_ShouldLoadBothEmployeesAndLocation() {
        ResponseEntity<DepartmentDTO> departmentResponse = testRestTemplate.getForEntity("/department/{departmentId}?includeEmployees=true&includeLocation=true", DepartmentDTO.class, "4");
        assertThat(departmentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepartmentDTO dept = departmentResponse.getBody();
        assertThat(dept.getDepartmentName()).isNotBlank();
        assertThat(dept.getEmployees()).isNotNull().hasSize(2);
        assertThat(dept.getLocation()).isNotNull();
    }


}
