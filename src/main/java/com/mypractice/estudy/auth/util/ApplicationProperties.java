package com.mypractice.estudy.auth.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private final AuthDto authDto = new AuthDto();
    private final OAuth2 oauth2Dto = new OAuth2();

    @Data
    public static class AuthDto {
        private String tokenSecret;
        private long tokenExpiration;
    }
    @Data
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

    }
}
