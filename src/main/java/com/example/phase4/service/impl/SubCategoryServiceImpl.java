package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Category;
import com.example.phase4.entity.SubCategory;
import com.example.phase4.repository.SubCategoryRepository;
import com.example.phase4.service.SubCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryServiceImpl extends BaseServiceImpl<SubCategory,Long> implements SubCategoryService {
    private final SubCategoryRepository repository;

    public SubCategoryServiceImpl(SubCategoryRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<SubCategory> findAllByCategory(Category category) {
        return repository.findAllByCategory(category);
    }

    @Override
    public Optional<SubCategory> findByNameAndCategory(String subCategoryName, Category category) {
        return repository.findByNameAndCategory(subCategoryName, category);
    }
}
