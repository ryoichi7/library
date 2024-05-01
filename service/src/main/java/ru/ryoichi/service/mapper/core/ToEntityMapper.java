package ru.ryoichi.service.mapper.core;

public interface ToEntityMapper<T, F> {
    F mapTo(T to);
}

