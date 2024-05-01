package ru.ryoichi.service.mapper.core;

public interface FromEntityMapper<F, T> {
    T mapFrom(F from);
}

