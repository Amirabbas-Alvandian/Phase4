package com.example.phase4.base.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T,R> {

    T saveOrUpdate(T t);

    void deleteById(R id);

    Optional<T> findById(R id);

    List<T> findAll();

    boolean validate(T t);
}
