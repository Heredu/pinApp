package com.pinApp.customerManagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pinApp.customerManagement.exception.ErrorResponse;
import com.pinApp.customerManagement.service.impl.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {
        try {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    } catch (ExpiredJwtException ex) {
        handleJwtException(response, request,
                "Token expirado",
                HttpStatus.UNAUTHORIZED,
                "JWT_EXPIRED");
    } catch (SignatureException ex) {
        handleJwtException(response, request,
                "Firma del token inválida",
                HttpStatus.UNAUTHORIZED,
                "INVALID_SIGNATURE");
    } catch (MalformedJwtException ex) {
        handleJwtException(response, request,
                "Token malformado",
                HttpStatus.BAD_REQUEST,
                "MALFORMED_JWT");
    } catch (BadCredentialsException ex) {
        handleJwtException(response, request,
                "Credenciales erróneas",
                HttpStatus.BAD_REQUEST,
                "AUTH_ERROR");
    } catch (Exception ex) {
            handleJwtException(response, request,
                    "Error de autenticación",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AUTH_ERROR");
        }

}
    private void handleJwtException(HttpServletResponse response,
                                    HttpServletRequest request,
                                    String message,
                                    HttpStatus status,
                                    String errorCode) throws IOException, java.io.IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(errorCode)
                .message(message)
                .path(request.getRequestURI())
                .build();
        log.error("Token exception: {}", message);
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}