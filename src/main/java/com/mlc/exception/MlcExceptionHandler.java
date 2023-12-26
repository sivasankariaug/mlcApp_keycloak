package com.mlc.exception;

import com.mlc.entity.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MlcExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MlcException.class)
    public ResponseEntity<Object> handleHealthException(MlcException ex) {
        return ResponseEntity.status(ex.getStausCode())
                .body(new Response(ex.getStausCode(), ex.getMessage()));

    }
}
