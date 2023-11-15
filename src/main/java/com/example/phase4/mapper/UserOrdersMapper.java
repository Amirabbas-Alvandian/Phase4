package com.example.phase4.mapper;

import com.example.phase4.dto.UserOrdersDTO;
import com.example.phase4.entity.Offer;
import com.example.phase4.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserOrdersMapper {

    UserOrdersMapper INSTANCE = Mappers.getMapper(UserOrdersMapper.class);

/*    @Mapping(source = "id",target = "offerId")
    @Mapping(source = "customerPrice",target = "price")
    @Mapping(source = "orderDate",target = "createDate")
    @Mapping(source = "startDate",target = "startDate")
    UserOrdersDTO orderModelToDTO(Order order);*/

    @Mapping(source = "id",target = "offerId")
    @Mapping(source = "specialistPrice",target = "price")
    @Mapping(source = "offerDate",target = "createDate")
    @Mapping(source = "startDate",target = "startDate")
    @Mapping(source = "startTime",target = "startTime")
    @Mapping(source = "finishTime",target = "finishTime")
    UserOrdersDTO offerModelToDTO(Offer offer);
}
