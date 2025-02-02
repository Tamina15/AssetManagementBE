package com.nashtech.rookies.assetmanagement.filter;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.nashtech.rookies.assetmanagement.config.CookieProperties;
import com.nashtech.rookies.assetmanagement.repository.TokenRepository;
import com.nashtech.rookies.assetmanagement.service.JwtService;
import com.nashtech.rookies.assetmanagement.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserService userService;

    private final HandlerExceptionResolver handleExceptionResolver;
    private final CookieProperties cookieProperties;

    public JwtAuthenticationFilter(JwtService jwtService, TokenRepository tokenRepository, UserService userService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handleExceptionResolver, CookieProperties cookieProperties) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.handleExceptionResolver = handleExceptionResolver;
        this.cookieProperties = cookieProperties;
    }

    @Value("${application.cookie.name}")
    private String cookieName;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            var tokenHeader = extractTokenFromHeader(request);
            var tokenCookie = extractTokenFromCookie(request);

            final String jwt;
            final String username;
            if ((tokenHeader == null || !tokenHeader.startsWith("Bearer ")) && tokenCookie == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // TODO: prioritize token in header
            if (tokenHeader != null) {
                jwt = tokenHeader.substring(7);
            } else {
                jwt = tokenCookie;
            }

            var isTokenValid = tokenRepository.findByToken(jwt).isPresent();
            if (!jwtService.isTokenExpired(jwt) && isTokenValid) {
                username = jwtService.extractUsername(jwt);
                UserDetails userDetails = this.userService.loadUserByUsername(username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication((authToken));
                }
            }
            filterChain.doFilter(request, response);
        } 
        catch (ExpiredJwtException e){
            Cookie cookie = new Cookie(cookieProperties.getName(), null);
            cookie.setMaxAge(0);
            cookie.setPath(cookieProperties.getPath());
            cookie.setSecure(cookieProperties.getSecure());
            response.addCookie(cookie);
            String cookieHeader = response.getHeader("Set-Cookie");
            cookieHeader+= "; SameSite=" + cookieProperties.getSameSite();
            response.setHeader("Set-Cookie", cookieHeader);
            handleExceptionResolver.resolveException(request, response, null, e);
        }
        catch (Exception e) {
            handleExceptionResolver.resolveException(request, response, null, e);
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
