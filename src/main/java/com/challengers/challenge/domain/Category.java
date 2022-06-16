package com.challengers.challenge.domain;

import java.util.Arrays;

public enum Category {
    EXERCISE, EATING_HABIT, LIFE, EMOTION, HOBBY, SURROUNDINGS, OTHER;

    public static Category of(String categoryStr) {
        return Arrays.stream(Category.values())
                .filter(category -> category.name().equals(categoryStr))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}
