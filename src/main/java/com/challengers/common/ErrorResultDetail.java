package com.challengers.common;


import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResultDetail extends ErrorResult{
    List<String> invalidFields;

    public ErrorResultDetail(String status, String message, List<String> invalidFields) {
        super(status, message);
        this.invalidFields =invalidFields;
    }
}
