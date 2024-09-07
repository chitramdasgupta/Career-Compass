package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthRequestUserMapper {
    @Mapping(target = "name", ignore = true)
    AuthRequestUserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "authorities", ignore = true)
    User toEntity(AuthRequestUserDto authRequestUserDto);
}
