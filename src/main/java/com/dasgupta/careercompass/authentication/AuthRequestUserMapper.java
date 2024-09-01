package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthRequestUserMapper {
    AuthRequestUserDto toDto(User user);

    User toEntity(AuthRequestUserDto authRequestUserDto);
}
