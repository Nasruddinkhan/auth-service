package com.mypractice.estudy.auth.service.impl;

import com.mypractice.estudy.auth.model.User;
import com.mypractice.estudy.auth.model.dto.SignUpRequestDto;
import com.mypractice.estudy.auth.repository.UserRepository;
import com.mypractice.estudy.auth.service.LoginService;
import com.mypractice.estudy.util.DocumentMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public LoginServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpRequestDto createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return DocumentMapperUtil.map(userRepository.save(user), SignUpRequestDto.class);
    }
}
