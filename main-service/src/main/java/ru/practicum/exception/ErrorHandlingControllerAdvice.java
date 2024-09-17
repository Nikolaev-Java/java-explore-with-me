package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException ex) {
        log.warn(ex.getMessage());
        return ApiError.builder().httpStatus(HttpStatus.NOT_FOUND).reason("Not found exception")
                .stackTrace(ex.getStackTrace())
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler({DuplicatedDataException.class, ConflictException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicatedDataException(final RuntimeException ex) {
        log.warn(ex.getMessage());
        return ApiError.builder()
                .httpStatus(HttpStatus.CONFLICT).reason(ex.getMessage())
                .stackTrace(ex.getStackTrace())
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler({Exception.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final Exception ex) {
        log.error(ex.getLocalizedMessage());
        return ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .reason(ex.getClass().getName())
                .stackTrace(ex.getStackTrace())
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    log.warn("{} - {}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
                    return ApiError.builder()
                            .reason(String.format("Invalid value of the %s parameter: %s",
                                    constraintViolation.getPropertyPath().toString(),
                                    constraintViolation.getMessage()))
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .stackTrace(ex.getStackTrace())
                            .message(ex.getMessage()).build();
                })
                .findFirst().orElse(ApiError.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST).reason(ex.getLocalizedMessage())
                        .stackTrace(ex.getStackTrace())
                        .message(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ApiError> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
                .map(error -> ApiError.builder()
                        .message(e.getMessage())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .stackTrace(e.getStackTrace())
                        .reason(error.getDefaultMessage())
                        .build()).toList();

    }

    @ExceptionHandler(HibernateError.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleSQLException(final HibernateError ex) {
        log.warn(ex.getMessage());
        return ApiError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .reason("SQL Exception")
                .stackTrace(ex.getStackTrace())
                .message(ex.getMessage()).build();
    }
}
