package com.example.phase4.mapper;

import com.example.phase4.dto.response.FilteredUserResponseDTO;
import com.example.phase4.dto.verifiedUserDTO;
import com.example.phase4.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper

public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id",target = "id")
    FilteredUserResponseDTO modelToDto(User user);

    verifiedUserDTO verifiedUser(User user);
}
