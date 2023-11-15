package com.example.phase4.repository;

import com.example.phase4.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
