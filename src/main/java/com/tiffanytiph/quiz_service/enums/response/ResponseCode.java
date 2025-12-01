package com.tiffanytiph.quiz_service.enums.response;

public enum ResponseCode {

    // HTTP Status Codes
    INTERNAL_ERROR,
    ACCESS_DENIED,
    BAD_REQUEST,
    NOT_FOUND,
    CREATED,
    UNSUPPORTED_MEDIA_TYPE,
    OK,
    UNAUTHORIZED,
    EMPTY_FILE,
    CONFLICT,

    // Application Status Code
    SERVICE_UNAVAILABLE,
    DB_ERROR,
    ENTITY,
    TOO_MANY_REQUEST,

    // Question Service Codes
    QUESTION_CREATED,
    QUESTION_NOT_FOUND,

    // Quiz Service Codes
    QUIZ_CREATED,
    QUIZ_NOT_FOUND
}
