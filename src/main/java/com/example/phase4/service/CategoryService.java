package com.example.phase4.service;

import com.example.phase4.base.service.BaseService;
import com.example.phase4.entity.Category;

import java.util.Optional;

public interface CategoryService extends BaseService<Category,Long> {
    Optional<Category> findByName(String name);

}
