package com.example.catalogmanager.error;

import com.example.catalogmanager.error.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @Autowired HttpServletRequest request;

  @ExceptionHandler
  public ResponseEntity<ErrorDto> handleGenericException(GeneralException exception) {
    logger.error(exception.getMessage(), exception);
    ErrorDto error =
        new ErrorDto(exception.getStatus(), exception.getMessage(), getOperationName(request));
    return ResponseEntity.status(exception.getStatus()).body(error);
  }

  @ExceptionHandler
  public ResponseEntity<List<ErrorDto>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    logger.error(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrors(exception));
  }

  @ExceptionHandler
  public ResponseEntity<List<ErrorDto>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    logger.error(exception.getMessage(), exception);
    ErrorDto error =
        new ErrorDto(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid parameter: %s".formatted(exception.getPropertyName()),
            getOperationName(request));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of(error));
  }

  private List<ErrorDto> getErrors(MethodArgumentNotValidException exception) {
    String operationName = getOperationName(request);

    List<ErrorDto> errors = new ArrayList<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError ->
                errors.add(
                    new ErrorDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid parameter: %s".formatted(fieldError.getField()),
                        operationName)));
    return errors;
  }

  private String getOperationName(HttpServletRequest request) {
    return request.getMethod() + " " + request.getRequestURI();
  }
}
