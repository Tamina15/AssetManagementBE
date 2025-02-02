package com.nashtech.rookies.assetmanagement.service.impl;

import com.nashtech.rookies.assetmanagement.repository.TokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;
    @Value("${application.cookie.name}")
    private String cookieName;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var tokenHeader = extractTokenFromHeader(request);
        var tokenCookie = extractTokenFromCookie(request);

        String jwt;

        //prioritize token in header
        if (tokenHeader != null) {
            jwt = tokenHeader.substring(7);
        } else {
            jwt = tokenCookie;
        }

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            tokenRepository.delete(storedToken);
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            return Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findFirst().map(Cookie::getValue).orElse(null);
        }

        return null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

}
