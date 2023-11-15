package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Category;
import com.example.phase4.entity.SubCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryService extends BaseService<SubCategory,Long> {

    List<SubCategory> findAllByCategory(Category category);

    Optional<SubCategory> findByNameAndCategory(String subCategoryName, Category category);
}
