package com.tiffanytiph.quiz_service.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.tiffanytiph.quiz_service.enums.response.ResponseCode;


@Service
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(final ResponseCode code, final String... params) {
        return this.messageSource.getMessage(code.name(), params, Locale.ENGLISH);
    }

}
