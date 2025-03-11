package com.healthcare.management.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.healthcare.management.dto.ConsultationDto;
import com.healthcare.management.entity.Consultation;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Custom mapping configuration
        modelMapper.addMappings(new PropertyMap<ConsultationDto, Consultation>() {
            @Override
            protected void configure() {
                map().getAppointment().setAppointment_id(source.getAppointmentId());
            }
        });

        return modelMapper;
    }
}