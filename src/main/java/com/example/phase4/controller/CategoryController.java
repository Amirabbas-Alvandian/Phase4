package com.example.phase4.controller;

import com.example.phase4.dto.request.CategoryRequestDTO;
import com.example.phase4.dto.response.CategoryResponseDTO;
import com.example.phase4.entity.Category;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.mapper.CategoryMapper;
import com.example.phase4.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@EnableWebSecurity
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController( CategoryService categoryService,
                               CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }


    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    //http://localHost/8080/category/register
    public ResponseEntity<CategoryResponseDTO> register(@RequestBody CategoryRequestDTO categoryRequestDTO){

            Category category = CategoryMapper.INSTANCE.DToToModel(categoryRequestDTO);
            Category savedCategory = categoryService.saveOrUpdate(category);


            CategoryResponseDTO responseDTO = CategoryMapper.INSTANCE.modelToDTO(savedCategory);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);


    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete (@PathVariable("id") long id){
        categoryService.deleteById(id);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SPECIALIST','CUSTOMER')")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("id") long id){
        Optional<Category> category = categoryService.findById(id);

        if(category.isEmpty())
            throw new EntityNotFoundException(String.format("category with id %d not found",id));


        CategoryResponseDTO categoryResponseDTO = CategoryMapper.INSTANCE.modelToDTO(
                category.get());
        System.out.println(categoryResponseDTO);
        return new ResponseEntity<>(categoryResponseDTO,HttpStatus.OK);
    }

    @GetMapping("/findByName/{name}")
    @PreAuthorize("hasAnyRole('ADMIN','SPECIALIST','CUSTOMER')")
    public ResponseEntity<CategoryResponseDTO> findByName(@PathVariable("name") String name){
        Optional<Category> category = categoryService.findByName(name);

        CategoryResponseDTO categoryResponseDTO = CategoryMapper.INSTANCE.modelToDTO(
                category.orElseThrow(() ->new EntityNotFoundException(
                        String.format("category with id %s not found",name))));
        return new ResponseEntity<>(categoryResponseDTO,HttpStatus.OK);
    }


    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN','SPECIALIST','CUSTOMER')")
    public ResponseEntity<List<CategoryResponseDTO>> findAll(){
/*        List<Category> Categories = categoryService.findAll();
        if (Categories.isEmpty())
            throw new EntityNotFoundException("no subCategory Found");*/
        List<CategoryResponseDTO> responseDTOList = categoryService.findAll().stream()
                .map(categoryMapper::modelToDTO).toList();

/*        for (Category c : Categories){
            responseDTOList.add(CategoryMapper.INSTANCE.modelToDTO(c));
        }*/
        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }
}
