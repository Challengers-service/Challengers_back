package com.challengers.common.exception;


import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeCastingException extends RuntimeException{

    public TypeCastingException(String fieldName, Enum[] e) {
        super(String.format("%s는 다음중 하나의 값이어야 합니다. [%s]",fieldName,
                Arrays.stream(e).map(Enum::name).collect(Collectors.joining(", "))));
    }

}
