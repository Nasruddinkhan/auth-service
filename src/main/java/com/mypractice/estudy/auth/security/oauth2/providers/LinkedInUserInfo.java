package com.mypractice.estudy.auth.security.oauth2.providers;

import com.mypractice.estudy.auth.security.oauth2.user.OAuth2UserInfo;

import java.util.Map;

public class LinkedInUserInfo extends OAuth2UserInfo {

    public LinkedInUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return  attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return "";
    }
}
