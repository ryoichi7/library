package ru.ryoichi.service.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class PredicateBuilder {
    private final List<Predicate> predicates = new ArrayList<>();

    public <T> PredicateBuilder add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate build() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }
}
