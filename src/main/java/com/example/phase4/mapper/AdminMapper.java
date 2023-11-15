package com.example.phase4.mapper;

import com.example.phase4.dto.request.AdminRequestDTO;
import com.example.phase4.dto.request.CategoryRequestDTO;
import com.example.phase4.dto.response.AdminResponseDTO;
import com.example.phase4.dto.response.CategoryResponseDTO;
import com.example.phase4.entity.Admin;
import com.example.phase4.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper extends GenericMapper<Admin, AdminRequestDTO, AdminResponseDTO>{
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);
}
