package com.tiffanytiph.quiz_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tiffanytiph.quiz_service.dto.BaseResponse;
import com.tiffanytiph.quiz_service.enums.response.ResponseCode;
import com.tiffanytiph.quiz_service.service.MessageService;

import jakarta.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    protected MessageService messageService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public ResponseEntity<BaseResponse> data(Object entity) {
        return data(ResponseCode.ENTITY, null, entity);
    }

    public ResponseEntity<BaseResponse> data(ResponseCode code, String message, Object entity) {
        return new ResponseEntity<>(BaseResponse.builder()
                .code(code)
                .message(message)
                .data(entity)
                .path(httpServletRequest.getRequestURI())
                .requestId(UUID.randomUUID().toString())
                .build(), code.name().contains(ResponseCode.CREATED.name())? HttpStatus.CREATED : HttpStatus.OK);
    }

    public ResponseEntity<BaseResponse> success(ResponseCode code, String... fields) {
        return new ResponseEntity<>(BaseResponse.builder()
                .code(code)
                .message(messageService.getMessage(code, fields))
                .path(httpServletRequest.getRequestURI())
                .requestId(UUID.randomUUID().toString())
                .build(), code.name().contains(ResponseCode.CREATED.name())? HttpStatus.CREATED : HttpStatus.OK);
    }
    
}
