package com.mypractice.estudy.auth.controller;

import com.mypractice.estudy.auth.model.User;
import com.mypractice.estudy.auth.model.dto.LoginRequestDto;
import com.mypractice.estudy.auth.model.dto.LoginResponseDto;
import com.mypractice.estudy.auth.model.dto.SignUpRequestDto;
import com.mypractice.estudy.auth.model.dto.SignUpResponseDto;
import com.mypractice.estudy.auth.security.provider.JwtTokenProvider;
import com.mypractice.estudy.auth.service.LoginService;
import com.mypractice.estudy.util.DocumentMapperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> registerUser(@RequestBody SignUpRequestDto signUpRequest) {
        log.info("register the user {}", signUpRequest);
        var user = DocumentMapperUtil.map(signUpRequest, User.class);
        var singResponse = loginService.createUser(user);
        var location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/${email}")
                .buildAndExpand(singResponse.getEmail()).toUri();
        return ResponseEntity.created(location)
                .body(new SignUpResponseDto(true, "User registered successfully@"));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var token =jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(new LoginResponseDto(token, "Bearer"));
    }

}
