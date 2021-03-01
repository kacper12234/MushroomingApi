package com.example.mushrooming.mapper;

import com.example.mushrooming.dto.RegisterRequest;
import com.example.mushrooming.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mapping(target = "enabled",defaultValue = "false")
    @Mapping(target = "password",expression = "java(passwordEncoder.encode(request.getPassword()))")
    public abstract User mapRequestToUser(RegisterRequest request);


}
