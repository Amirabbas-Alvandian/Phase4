package com.example.phase4.controller;

import com.example.phase4.dto.request.SubCategoryRequestDTO;
import com.example.phase4.dto.response.SubCategoryResponseDTO;
import com.example.phase4.entity.Category;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.mapper.SubCategoryMapper;
import com.example.phase4.service.CategoryService;
import com.example.phase4.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subCategory")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SubCategoryController {

    private final SubCategoryService service;
    private final CategoryService categoryService;
    private final SubCategoryMapper subCategoryMapper;

    @PostMapping("/register/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryResponseDTO> register(@PathVariable("category") String categoryName, @RequestBody SubCategoryRequestDTO requestDTO){

        Category category = findCategoryByName(categoryName);

        SubCategory subCategory = SubCategoryMapper.INSTANCE.DToToModel(requestDTO);

        subCategory.setCategory(category);

        SubCategory savedSubCategory = service.saveOrUpdate(subCategory);

        SubCategoryResponseDTO responseDTO = SubCategoryMapper.INSTANCE.modelToDTO(savedSubCategory);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/findByName/{category}/{subCategory}")
    @PreAuthorize("hasAnyRole('ADMIN','SPECIALIST','CUSTOMER')")
    public ResponseEntity<SubCategoryResponseDTO> findByName(@PathVariable("category") String categoryName
                                                            ,@PathVariable("subCategory") String subcategoryName){

        Category category = findCategoryByName(categoryName);

        SubCategory subCategory = findSubCategoryByName(subcategoryName
                , category);

        SubCategoryResponseDTO responseDTO = SubCategoryMapper.INSTANCE.modelToDTO(subCategory);

        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN','SPECIALIST','CUSTOMER')")
    public ResponseEntity<List<SubCategoryResponseDTO>> findAll(){
/*        List<SubCategory> subCategories = service.findAll();
        if (subCategories.isEmpty())
            throw new EntityNotFoundException("no subCategory Found");*/
        List<SubCategoryResponseDTO> responseDTOList = service.findAll().stream()
                .map(subCategoryMapper::modelToDTO).toList();

/*        for (SubCategory s : subCategories){
            responseDTOList.add(SubCategoryMapper.INSTANCE.modelToDTO(s));
        }*/
        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }

/*    @PutMapping("/update/price")
    public ResponseEntity<SubCategoryResponseDTO> updatePrice(@RequestBody SubCategoryUpdatePriceRequestDTO requestDTO){
        Category category = findCategoryByName(requestDTO.categoryName());

        SubCategory subCategory = findSubCategoryByName(requestDTO.subCategoryName(), category);


    }*/


    public Category findCategoryByName(String categoryName){
        return categoryService.findByName(categoryName).orElseThrow(
                () -> new EntityNotFoundException(String.format("category %s does not exist",categoryName)
        ));
    }

    public SubCategory findSubCategoryByName(String subcategoryName,Category category){
        return service.findByNameAndCategory(subcategoryName,category).orElseThrow(
                () -> new EntityNotFoundException(String.format("subcategory %s does not exist",subcategoryName))
                );
    }
}
