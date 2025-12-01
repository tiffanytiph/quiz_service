package com.tiffanytiph.quiz_service.dto;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiffanytiph.quiz_service.enums.response.ResponseCode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BaseResponse {

    @Builder.Default
    Date date = new Date();
    ResponseCode code;

    // @JsonDeserialize(as = String.class)
    Object message;

    // @JsonDeserialize(as = String.class)
    Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> errors;
    String path;
    String requestId;

}
