package com.dasgupta.careercompass.authentication;

import com.dasgupta.careercompass.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthRequestUserMapper {
    AuthRequestUserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(AuthRequestUserDto authRequestUserDto);
}
