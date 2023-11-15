package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Category;
import com.example.phase4.repository.CategoryRepository;
import com.example.phase4.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category,Long> implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }
}
