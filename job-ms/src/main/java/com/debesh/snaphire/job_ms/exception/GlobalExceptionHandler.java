package com.debesh.snaphire.job_ms.exception;


import com.debesh.snaphire.job_ms.model.ErrorOutputModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handling global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutputModel> handleGlobalException(Exception e) {
        ErrorOutputModel error = new ErrorOutputModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handling Job Not Found Exception
    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ErrorOutputModel> handleJobNotFoundException(Exception e) {
        ErrorOutputModel error = new ErrorOutputModel(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
