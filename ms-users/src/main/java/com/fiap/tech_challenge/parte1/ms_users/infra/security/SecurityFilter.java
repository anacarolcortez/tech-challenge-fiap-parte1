package com.fiap.tech_challenge.parte1.ms_users.infra.security;

import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import com.fiap.tech_challenge.parte1.ms_users.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Security filter that intercepts every HTTP request to authenticate users based on JWT tokens.
 * <p>
 * This filter extracts the JWT token from the Authorization header, validates it, retrieves the user details,
 * and sets the authentication in the SecurityContext if the token and user are valid.
 * </p>
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserRepository repository;

    /**
     * Constructs a SecurityFilter with the required TokenService and UserRepository.
     *
     * @param tokenService the service responsible for JWT token operations such as validation and extraction of claims
     * @param repository   the user repository used to load user details from the database
     */
    public SecurityFilter(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    /**
     * Filters incoming HTTP requests to authenticate users based on the JWT token provided in the Authorization header.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to pass the request and response to the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an input or output error is detected
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenJWT = retrieveToken(request);
        if (tokenJWT != null) {
            String subject = tokenService.extractUserLoginFromToken(tokenJWT);
            UserDetails userDetails = repository.findByLogin(subject)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Retrieves the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the HTTP request
     * @return the JWT token string without the "Bearer " prefix, or null if the header is missing or malformed
     */
    private String retrieveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
