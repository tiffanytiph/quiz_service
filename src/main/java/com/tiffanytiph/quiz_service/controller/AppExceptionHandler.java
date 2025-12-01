package com.tiffanytiph.quiz_service.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.authorization.AuthorizationDeniedException;
// import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tiffanytiph.quiz_service.dto.BaseResponse;
import com.tiffanytiph.quiz_service.enums.response.ResponseCode;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler{

    private static final HttpHeaders httpHeaders = new HttpHeaders();

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, 
        WebRequest request) {
        Map<Integer, List<String>> violationsByRow = new HashMap<>();

        exception.getConstraintViolations().forEach(violation -> {
            Matcher matcher = Pattern.compile("\\[(\\d+)\\]").matcher(violation.getPropertyPath().toString());
            if (matcher.find()) {
                int rowIndex = Integer.parseInt(matcher.group(1));
                String message = new StringBuilder("row ")
                    .append(rowIndex + 1)
                    .append(": ")
                    .append(violation.getMessage())
                    .toString();
                violationsByRow.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(message);
            }
        });

        List<String> violations = violationsByRow.entrySet()
            .stream()
            .flatMap(entry -> entry.getValue().stream())
            .collect(Collectors.toList());
        
        return handleExceptionInternal(exception, 
            buildResponse(ResponseCode.BAD_REQUEST,
                new StringBuilder("Constraint violation: ")
                .append(String.join("; ", violations)).toString(), request), 
                    httpHeaders, 
                    HttpStatus.BAD_REQUEST, 
                    request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDaoException(DataIntegrityViolationException exception, WebRequest request) {
        log.error("Database Exception", exception);
        return handleExceptionInternal(exception, buildResponse(ResponseCode.DB_ERROR,
                        NestedExceptionUtils.getMostSpecificCause(exception).getMessage(), request),
                httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, 
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Method Args Exception", exception);
        
        String message;
        try {
            message = exception.getBindingResult().getFieldError().getDefaultMessage();
        } catch (Exception e) {
            message = exception.getBindingResult().toString();
        }
        return handleExceptionInternal(exception, 
            buildResponse(ResponseCode.BAD_REQUEST, message, request), 
            headers, 
            HttpStatus.BAD_REQUEST, 
            request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, 
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpMessageNotReadable Exception", exception);
        
        String message = null;
        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                message = String.format("Invalid enum value for the field: '%s'. The value must be one of: %s.",
                        ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(), 
                        Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        return handleExceptionInternal(exception, 
            buildResponse(ResponseCode.BAD_REQUEST, message, request), 
            headers, 
            HttpStatus.BAD_REQUEST, 
            request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception exception, WebRequest request) {
        log.error("Generic Exception", exception);
        
        return handleExceptionInternal(exception, 
            buildResponse(ResponseCode.INTERNAL_ERROR, exception.getMessage(), request),
                httpHeaders, 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                request);
    }

    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request, 
    //     HttpServletResponse response) {
    //     try {
    //         response.setStatus(HttpStatus.FORBIDDEN.value());
    //         return handleExceptionInternal(exception, 
    //             buildResponse(ResponseCode.ACCESS_DENIED, 
    //                 "You do not have permission to perform this action.", request),
    //                 httpHeaders, 
    //                 HttpStatus.FORBIDDEN, 
    //                 request);
    //     } catch (Exception e) {
    //         return handleExceptionInternal(exception, 
    //             buildResponse(ResponseCode.SERVICE_UNAVAILABLE, exception.getMessage(), request),
    //                 httpHeaders, 
    //                 HttpStatus.SERVICE_UNAVAILABLE, 
    //                 request);
    //     }
    // }

    @ExceptionHandler(HttpClientErrorException.UnsupportedMediaType.class)
    public ResponseEntity<Object> unsupportedMediaTypeException(HttpClientErrorException.UnsupportedMediaType exception, 
        WebRequest request) {
        return handleExceptionInternal(exception, 
            buildResponse(ResponseCode.UNSUPPORTED_MEDIA_TYPE, 
            "Unsupported media type", request),
                httpHeaders, 
                HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
                request);
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<Object> handleInvalidDataAccessResourceUsageException(
            InvalidDataAccessResourceUsageException exception, WebRequest request) {
        // Todo: This is temporary and may be replaced with a more robust solution later.
        if (exception.getMessage().contains("relation") && exception.getMessage().contains("does not exist")) {
            return handleExceptionInternal(exception, 
                buildResponse(ResponseCode.NOT_FOUND, "The requested resource is unavailable.", request), 
                httpHeaders, 
                HttpStatus.NOT_FOUND, 
                request);
        } else {
            throw exception;
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        log.error("Entity Not Found", exception);
        return handleExceptionInternal(
            exception,
            buildResponse(ResponseCode.NOT_FOUND, exception.getMessage(), request),
            new HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
        log.error("Bad Request", exception);
        return handleExceptionInternal(
            exception,
            buildResponse(ResponseCode.BAD_REQUEST, exception.getMessage(), request),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException exception, WebRequest request) {
        log.error("Null Pointer Exception", exception);
        return handleExceptionInternal(
            exception,
            buildResponse(ResponseCode.BAD_REQUEST, "A required value was missing or null", request),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST,
            request
        );
    }

    // @ExceptionHandler(AuthenticationException.class)
    // public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
    //     log.error("Authentication Failed", exception);
    //     return handleExceptionInternal(
    //         exception,
    //         buildResponse(ResponseCode.UNAUTHORIZED, exception.getMessage(), request),
    //         new HttpHeaders(),
    //         HttpStatus.UNAUTHORIZED,
    //         request
    //     );
    // }

    // @ExceptionHandler(AuthorizationDeniedException.class)
    // public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException exception, 
    //     WebRequest request) {
    //     log.error("Authorization Denied", exception);
    //     return handleExceptionInternal(
    //         exception,
    //         buildResponse(ResponseCode.UNAUTHORIZED, exception.getMessage(), request),
    //         new HttpHeaders(),
    //         HttpStatus.UNAUTHORIZED,
    //         request
    //     );
    // }

    // @ExceptionHandler(RequestNotPermitted.class)
    // public ResponseEntity<Object> handleRateLimitExceeded(
    //         RequestNotPermitted exception,
    //         WebRequest request
    // ) {
    //     log.error("Request Not Permitted", exception);
    //     return handleExceptionInternal(
    //             exception,
    //             buildResponse(ResponseCode.TOO_MANY_REQUEST, exception.getMessage(), request),
    //             new HttpHeaders(),
    //             HttpStatus.TOO_MANY_REQUESTS,
    //             request
    //     );
    // }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException exception,
            WebRequest request
    ) {
        log.error("BAD REQUEST", exception);
        return handleExceptionInternal(
                exception,
                buildResponse(ResponseCode.BAD_REQUEST, exception.getMessage(), request),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }
    
    private BaseResponse buildResponse(ResponseCode code, String message, WebRequest request) {
        return BaseResponse.builder()
                .code(code)
                .message(message)
                .path(request.getContextPath())
                .requestId(UUID.randomUUID().toString())
                .errors(null)
                .build();
    }

}
