package com.mypractice.estudy.auth.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {
    private CookieUtils(){}

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        var cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
           return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findAny();
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        var cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Arrays.stream(cookies).filter(cookie-> cookie.getName().equals(name))
                    .map(CookieUtils::getCookie).forEach(response::addCookie);
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())));
    }


    private static Cookie getCookie(Cookie cookie) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
