package com.example.phase4.mapper;


import com.example.phase4.dto.request.CategoryRequestDTO;
import com.example.phase4.dto.response.CategoryResponseDTO;
import com.example.phase4.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper extends GenericMapper<Category, CategoryRequestDTO, CategoryResponseDTO>{

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
