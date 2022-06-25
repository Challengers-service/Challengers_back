package com.challengers.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FollowException extends RuntimeException {

    public FollowException(String message) {
        super(message);
    }
}
