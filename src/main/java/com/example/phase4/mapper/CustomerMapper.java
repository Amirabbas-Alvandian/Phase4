package com.example.phase4.mapper;

import com.example.phase4.dto.request.CustomerRequestDTO;
import com.example.phase4.dto.response.CustomerResponseDTO;
import com.example.phase4.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper extends GenericMapper<Customer,CustomerRequestDTO,CustomerResponseDTO> {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

}
