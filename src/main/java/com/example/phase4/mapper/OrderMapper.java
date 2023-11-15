package com.example.phase4.mapper;

import com.example.phase4.dto.request.OrderRequestDTO;
import com.example.phase4.dto.response.OrderResponseDTO;
import com.example.phase4.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper extends GenericMapper<Order, OrderRequestDTO, OrderResponseDTO>{

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Override
    @Mapping(target = "address.province",source = "addressDTO.province")
    @Mapping(target = "address.city",source = "addressDTO.city")
    @Mapping(target = "address.street",source = "addressDTO.street")
    @Mapping(target = "address.houseName",source = "addressDTO.houseName")
    @Mapping(target = "address.unit",source = "addressDTO.unit")
    Order DToToModel(OrderRequestDTO requestDTO);


    @Override
    @Mapping(target = "customerResponseDTO",source = "customer")
    @Mapping(target = "addressDTO",source = "address")
    OrderResponseDTO modelToDTO(Order order);
}
