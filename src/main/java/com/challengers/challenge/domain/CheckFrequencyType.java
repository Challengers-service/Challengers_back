package com.challengers.challenge.domain;

import com.challengers.common.exception.TypeCastingException;

import java.util.Arrays;

public enum CheckFrequencyType {
    EVERY_DAY, EVERY_WEEK, OTHERS;

    public static CheckFrequencyType of(String checkFrequencyStr) {
        return Arrays.stream(CheckFrequencyType.values())
                .filter(checkFrequencyType -> checkFrequencyType.name().equals(checkFrequencyStr))
                .findFirst()
                .orElseThrow(()->new TypeCastingException("CheckFrequencyType",values()));
    }
}
