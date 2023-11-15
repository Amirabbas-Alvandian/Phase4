package com.example.phase4.mapper;

public interface GenericMapper<T,R,U> {
    T DToToModel(R r);

    U modelToDTO(T t);
}
