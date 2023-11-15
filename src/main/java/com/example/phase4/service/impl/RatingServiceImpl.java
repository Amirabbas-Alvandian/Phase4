package com.example.phase4.service.impl;

import com.example.phase4.base.service.impl.BaseServiceImpl;
import com.example.phase4.entity.Rating;
import com.example.phase4.repository.RatingRepository;
import com.example.phase4.service.RatingService;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl extends BaseServiceImpl<Rating,Long> implements RatingService {
    private final RatingRepository repository;

    public RatingServiceImpl(RatingRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
