package com.example.phase4.mapper;

import com.example.phase4.dto.request.OfferRequestDTO;
import com.example.phase4.dto.response.OfferResponseDTO;
import com.example.phase4.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper extends GenericMapper<Offer, OfferRequestDTO, OfferResponseDTO>{

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    @Override
    Offer DToToModel(OfferRequestDTO requestDTO);

    @Override
    @Mapping(source = "specialist.firstName",target = "specialistFirstName" )
    @Mapping(source = "specialist.lastName",target = "specialistLastName" )
    @Mapping(source = "specialist.score",target = "specialistScore" )
    @Mapping(source = "order.id",target = "orderId")
    @Mapping(source = "startDate",target = "startDate")
    OfferResponseDTO modelToDTO(Offer offer);
}
