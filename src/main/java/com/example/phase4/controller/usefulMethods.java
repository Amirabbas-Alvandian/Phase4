package com.example.phase4.controller;

import com.example.phase4.entity.Category;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.service.CategoryService;
import com.example.phase4.service.SubCategoryService;
import org.springframework.stereotype.Component;

@Component
public class usefulMethods {

    private final SubCategoryService service;
    private final CategoryService categoryService;

    public usefulMethods(SubCategoryService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }


    public Category findCategoryByName(String categoryName){
        return categoryService.findByName(categoryName).orElseThrow(
                () -> new EntityNotFoundException(String.format("category %s does not exist",categoryName)
                ));
    }

    public SubCategory findSubCategoryByName(String subcategoryName, Category category){
        return service.findByNameAndCategory(subcategoryName,category).orElseThrow(
                () -> new EntityNotFoundException(String.format("subcategory %s does not exist",subcategoryName))
        );
    }
}
