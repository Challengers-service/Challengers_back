package com.challengers.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserException extends RuntimeException{

    public UserException(){
        super("해당 사용자를 찾을 수 없음.");
    }
    public UserException(String message){
        super(message);
    }

}
