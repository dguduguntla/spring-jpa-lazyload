package com.gdp.lazyload;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.entity.Department;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class LazyLoadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyLoadingApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(EntityManagerFactory emFactory) {
        ModelMapper mapper = new ModelMapper();
        //Add a global configuration to fetch handle the lazily loaded fields.
        mapper.getConfiguration().setPropertyCondition(context -> emFactory.getPersistenceUnitUtil().isLoaded(context.getSource()));
        //department.location field is loaded depending upon input so we are excluding this field explicitly
        mapper.typeMap(Department.class, DepartmentDTO.class).addMappings(new PropertyMap<Department, DepartmentDTO>() {
            @Override
            protected void configure() {
                map().setLocation(null);
            }
        });
        return mapper;
    }

}
