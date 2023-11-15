package com.example.phase4.mapper;

import com.example.phase4.dto.request.RatingRequestDTO;
import com.example.phase4.dto.response.RatingResponseDTO;
import com.example.phase4.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper extends GenericMapper<Rating, RatingRequestDTO, RatingResponseDTO>{

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);
}
