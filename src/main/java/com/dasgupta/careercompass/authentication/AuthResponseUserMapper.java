package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthResponseUserMapper {
    AuthResponseUserDto toDto(User user);

    User toEntity(AuthResponseUserDto authResponseUserDto);
}
