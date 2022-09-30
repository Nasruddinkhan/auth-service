package com.mypractice.estudy.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public record ErrorDetailsDto(
    @JsonProperty("error_message") String errorMessage,
    @JsonProperty("error_code") int errorCode, Date date){
}
