package com.example.phase4.mapper;

import com.example.phase4.dto.AddressDTO;
import com.example.phase4.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);


    AddressDTO DToToModel(Address address);

    Address modelToDTO(AddressDTO addressDTO);
}
