package org.donis.models.mappers;

import org.donis.models.dto.UserDTO;
import org.donis.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
}
