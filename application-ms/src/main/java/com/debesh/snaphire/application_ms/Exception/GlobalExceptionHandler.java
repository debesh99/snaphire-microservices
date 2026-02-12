package com.debesh.snaphire.application_ms.Exception;

import com.debesh.snaphire.application_ms.model.ErrorOutputModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handling global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutputModel> handleGlobalException(Exception e) {
        ErrorOutputModel error = new ErrorOutputModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Handling Company Not Found Exception
    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorOutputModel> handleApplicationNotFoundException(Exception e) {
        ErrorOutputModel error = new ErrorOutputModel(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
