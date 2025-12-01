package com.tiffanytiph.quiz_service.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tiffanytiph.quiz_service.enums.response.ResponseCode;

public abstract class AppService {

    @Autowired
    private MessageService messageService;

    protected String getMessage(ResponseCode code, String... params) {
        return messageService.getMessage(code, params);
    }
    
}