package com.mypractice.estudy.auth.security.oauth2.user;


import com.mypractice.estudy.auth.security.oauth2.providers.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory() {
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        System.out.println(attributes);
        return switch (registrationId) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            case "twitter" -> new TwitterOAuth2UserInfo(attributes);
            case "facebook" -> new FacebookOAuth2UserInfo(attributes);
            case "linkedIn" -> new LinkedInUserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");

        };

    }
}
