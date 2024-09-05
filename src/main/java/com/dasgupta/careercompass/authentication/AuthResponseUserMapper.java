package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthResponseUserMapper {
    AuthResponseUserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(AuthResponseUserDto authResponseUserDto);
}
