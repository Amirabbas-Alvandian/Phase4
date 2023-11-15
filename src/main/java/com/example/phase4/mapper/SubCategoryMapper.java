package com.example.phase4.mapper;

import com.example.phase4.dto.request.SubCategoryRequestDTO;
import com.example.phase4.dto.response.SubCategoryResponseDTO;
import com.example.phase4.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubCategoryMapper extends GenericMapper<SubCategory, SubCategoryRequestDTO, SubCategoryResponseDTO>{

    SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);

    @Override
    @Mapping(target = "categoryName",source = "category.name")
    SubCategoryResponseDTO modelToDTO(SubCategory subCategory);
}
