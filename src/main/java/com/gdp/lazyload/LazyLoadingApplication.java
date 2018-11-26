package com.gdp.lazyload;

import com.gdp.lazyload.domain.DepartmentDTO;
import com.gdp.lazyload.entity.Department;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

@SpringBootApplication
public class LazyLoadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyLoadingApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(EntityManagerFactory emFactory) {
        ModelMapper mapper = new ModelMapper();
        final PersistenceUnitUtil unitUtil = emFactory.getPersistenceUnitUtil();

        mapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return unitUtil.isLoaded(context.getSource());
            }
        });

        //department.location field is loaded depending upon input so we are excluding this field and let it be handled
        //depending upon the case.
        mapper.typeMap(Department.class, DepartmentDTO.class).addMappings(new PropertyMap<Department, DepartmentDTO>() {
            @Override
            protected void configure() {
                map().setLocation(null);
            }
        });

        return mapper;
    }

}
