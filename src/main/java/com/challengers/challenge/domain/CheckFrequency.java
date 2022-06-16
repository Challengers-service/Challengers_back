package com.challengers.challenge.domain;

import java.util.Arrays;

public enum CheckFrequency {
    EVERY_DAY, ONCE, TWICE, THREE, FOUR, FIVE, SIX;

    public static CheckFrequency of(String checkFrequencyStr) {
        return Arrays.stream(CheckFrequency.values())
                .filter(checkFrequency -> checkFrequency.name().equals(checkFrequencyStr))
                .findFirst()
                .orElseThrow(()->new RuntimeException());
    }
}
