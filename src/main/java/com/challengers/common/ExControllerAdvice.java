package com.challengers.common;

import com.challengers.common.exception.NotFoundException;
import com.challengers.common.exception.TypeCastingException;
import com.challengers.common.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> illegalStateError(IllegalStateException e) {
        log.error("error message = {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResult("400",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 공통 에러
    @ExceptionHandler
    public ResponseEntity<ErrorResult> unAuthorizationError(UnAuthorizedException e) {
        log.error("error message = {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResult("403",e.getMessage()), HttpStatus.FORBIDDEN);
    }

    // 공통 에러
    @ExceptionHandler
    public ResponseEntity<ErrorResult> notFoundError(NotFoundException e) {
        log.error("error message = {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResult("404",e.getMessage()), HttpStatus.NOT_FOUND);
    }

    // 공통 에러
    @ExceptionHandler
    public ResponseEntity<ErrorResult> typeConvertError(TypeCastingException e) {
        log.error("error message = {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResult("400",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 공통 에러
    @ExceptionHandler
    public ResponseEntity<ErrorResult> BindingError(BindException e) {
        String responseErrorMessage = e.getFieldErrors().stream().map(this::bindErrorMessageConvert)
                .collect(Collectors.joining(", "));
        List<String> invalidFields = e.getFieldErrors().stream().map(FieldError::getField)
                .collect(Collectors.toList());

        log.error("error message = {}", e.getMessage());
        log.error("response error message = {}", responseErrorMessage);
        log.error("invalid fields = {}", invalidFields);

        return new ResponseEntity<>(new ErrorResultDetail("400", responseErrorMessage, invalidFields), HttpStatus.BAD_REQUEST);
    }

    private String bindErrorMessageConvert(FieldError fieldError) {
        switch (Objects.requireNonNull(fieldError.getCode())) {
            case "NotNull":  return String.format("%s는 필수값 입니다", fieldError.getField());
            case "NotBlank":  return String.format("%s는 공백이어서는 안됩니다", fieldError.getField());
            case "typeMismatch": return String.format("%s의 타입이 올바르지 않습니다", fieldError.getField());
            default: return String.format("%s는 %s",fieldError.getField(),fieldError.getDefaultMessage());
        }
    }
}
