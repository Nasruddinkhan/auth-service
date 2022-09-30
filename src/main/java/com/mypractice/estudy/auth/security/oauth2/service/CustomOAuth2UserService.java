package com.mypractice.estudy.auth.security.oauth2.service;


import com.mypractice.estudy.auth.model.AuthProvider;
import com.mypractice.estudy.auth.model.User;
import com.mypractice.estudy.auth.repository.UserRepository;
import com.mypractice.estudy.auth.security.oauth2.user.OAuth2UserInfo;
import com.mypractice.estudy.auth.security.oauth2.user.OAuth2UserInfoFactory;
import com.mypractice.estudy.auth.security.oauth2.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        var oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }


    private OAuth2User processOAuth2User(final OAuth2UserRequest oAuth2UserRequest, final OAuth2User oAuth2User) {
        var oAuth2UserInfo = Optional.ofNullable(OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()))
                .orElseThrow(() -> new OAuth2AuthenticationException("Email not found from OAuth2 provider"));
        var user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(usr -> updateUser(usr, oAuth2UserInfo, oAuth2UserRequest)).orElseGet(() -> addUser(oAuth2UserRequest, oAuth2UserInfo));
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User addUser(final OAuth2UserRequest oAuth2UserRequest, final OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(User.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .emailVerified(true)
                .imageUrl(oAuth2UserInfo.getImageUrl()).build());
    }

    private User updateUser(final User existingUser, final OAuth2UserInfo oAuth2UserInfo, final OAuth2UserRequest oAuth2UserRequest) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setProviderId(oAuth2UserInfo.getId());
        existingUser.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        return userRepository.save(existingUser);
    }
}
