package com.example.phase4.base.service.impl;


import com.example.phase4.base.service.BaseService;
import com.example.phase4.validation.EntityValidator;
import jakarta.persistence.NoResultException;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BaseServiceImpl<T,R> implements BaseService<T,R> {


    private final JpaRepository<T,R> repository;
    private final Validator validator;

    public BaseServiceImpl(JpaRepository<T, R> repository) {
        this.repository = repository;
        this.validator = EntityValidator.validator;
    }


    @Override
    public T saveOrUpdate(T t) {
        if (!validate(t)){
            return null;
        }
        try {
            return repository.save(t);
        }catch (RollbackException r){
            System.out.println(r.getMessage());
            return null;
        }
    }

    @Override
    public void deleteById(R id) {
        try {
            repository.deleteById(id);
        }catch (NoResultException r){
            System.out.println(r.getMessage());
        }
    }

    @Override
    public Optional<T> findById(R id) {
        try {
            return repository.findById(id);
        }catch (NoResultException r){
            System.out.println(r.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean validate(T t) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(t) ;
        if(!violationSet.isEmpty()){
            for (ConstraintViolation<T> c : violationSet){
                System.out.println(c.getMessage());
            }
            return false;
        }
        return true;
    }
}
