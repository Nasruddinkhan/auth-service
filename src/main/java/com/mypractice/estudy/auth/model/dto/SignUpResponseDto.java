package com.mypractice.estudy.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class SignUpResponseDto {
    private boolean success;
    private String message;

}
