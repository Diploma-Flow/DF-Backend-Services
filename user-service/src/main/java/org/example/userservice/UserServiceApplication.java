package org.example.userservice;

import org.example.userservice.dto.UserDto;
import org.example.userservice.mapper.UserAccountToUserDtoMap;
import org.example.userservice.model.UserAccount;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addMappings(new UserAccountToUserDtoMap());

        return modelMapper;
    }
}
