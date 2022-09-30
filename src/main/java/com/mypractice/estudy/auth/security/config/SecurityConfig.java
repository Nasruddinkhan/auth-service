package com.mypractice.estudy.auth.security.config;

import com.mypractice.estudy.auth.security.CustomUserDetailsService;
import com.mypractice.estudy.auth.security.RestAuthenticationEntryPoint;
import com.mypractice.estudy.auth.security.filter.JwtTokenAuthenticationFilter;
import com.mypractice.estudy.auth.security.oauth2.cookies.HttpCookiesRequestRepository;
import com.mypractice.estudy.auth.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.mypractice.estudy.auth.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.mypractice.estudy.auth.security.oauth2.service.CustomOAuth2UserService;
import com.mypractice.estudy.auth.security.provider.JwtTokenProvider;
import com.mypractice.estudy.auth.util.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ApplicationProperties appProperties;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return   http.authorizeHttpRequests(authz -> authz.
                        anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(httpCookiesRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                //.and()
                //.tokenEndpoint()
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler())
                .and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public HttpCookiesRequestRepository httpCookiesRequestRepository() {
        return new HttpCookiesRequestRepository();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js","/actuator/**", "/user/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/api-docs/**");
    }
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(tokenProvider,  appProperties, httpCookiesRequestRepository());
    }
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(httpCookiesRequestRepository());
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtTokenAuthenticationFilter tokenAuthenticationFilter() {
        return new JwtTokenAuthenticationFilter(tokenProvider, userDetailsService);
    }
}
