package com.example.phase4.repository;

import com.example.phase4.entity.Category;
import com.example.phase4.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findAllByCategory(Category category);

    Optional<SubCategory> findByNameAndCategory(String subCategoryName, Category category);
}
