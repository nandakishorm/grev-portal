package com.nand.grievance.publisher.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
