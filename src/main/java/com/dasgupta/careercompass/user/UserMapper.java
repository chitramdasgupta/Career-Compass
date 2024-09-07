package com.dasgupta.careercompass.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserDto userDto);
}
