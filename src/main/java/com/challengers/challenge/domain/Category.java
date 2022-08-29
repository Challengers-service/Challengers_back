package com.challengers.challenge.domain;

import com.challengers.common.exception.TypeCastingException;

import java.util.Arrays;

public enum Category {
    LIFE, STUDY, WORK_OUT, SELF_DEVELOPMENT;

    public static Category of(String categoryStr) {
        return Arrays.stream(Category.values())
                .filter(category -> category.name().equals(categoryStr))
                .findFirst()
                .orElseThrow(() -> new TypeCastingException("Category", values()));
    }
}
