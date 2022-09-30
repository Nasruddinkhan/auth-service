package com.mypractice.estudy.auth.service;

import com.mypractice.estudy.auth.model.User;
import com.mypractice.estudy.auth.model.dto.SignUpRequestDto;

public interface LoginService {
    SignUpRequestDto createUser(User user);
}
