package com.example.phase4.mapper;

import com.example.phase4.dto.request.SpecialistRequestDTO;
import com.example.phase4.dto.response.SpecialistRegisterResponseDTO;
import com.example.phase4.dto.response.SpecialistResponseDTO;
import com.example.phase4.dto.response.SubCategoryResponseDTO;
import com.example.phase4.entity.Specialist;
import com.example.phase4.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Mapper
public interface SpecialistMapper extends GenericMapper<Specialist, SpecialistRequestDTO, SpecialistResponseDTO> {

    SpecialistMapper INSTANCE = Mappers.getMapper(SpecialistMapper.class);


    @Override
    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "password",target = "password")
    @Mapping(source = "email",target = "email")
    Specialist DToToModel(SpecialistRequestDTO requestDTO);

    @Override
    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "signUpDate",target = "signUpDate")
    SpecialistResponseDTO modelToDTO(Specialist specialist);

    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "signUpDate",target = "signUpDate")
    SpecialistRegisterResponseDTO registeredModelToDTO(Specialist specialist);

    default byte[] imageFileToByte(MultipartFile image) throws IOException {

        return image.getBytes();
    }

    default List<SubCategoryResponseDTO> subModelToDTO(List<SubCategory> subCategories){
        return subCategories.stream().map(SubCategoryMapper.INSTANCE::modelToDTO).toList();
    }
}
