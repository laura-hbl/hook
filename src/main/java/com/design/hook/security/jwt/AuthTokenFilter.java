package com.design.hook.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom AuthTokenFilter class that extends OncePerRequestFilter and overrides doFilterInternal() method.
 * Filter that executes once per request.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * Gets JWT from header, if the request has JWT, validate it and parse username from it. Then from
     * username, get UserDetails to create an Authentication object and set the current Authentication object in
     * SecurityContext.
     *
     * @param request     HttpServletRequest reference
     * @param response    HttpServletResponse reference
     * @param filterChain FilterChain reference
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        try {
            var bearer = request.getHeader("Authorization");
            if (bearer != null) {
                // Validates the JWT if not null
                var token = bearer.split("Bearer ")[1];
                if (jwtUtils.validateJwtToken(token)) {
                    // Parses username from the JWT
                    String username = jwtUtils.getUserNameFromJwtToken(token);

                    // Gets UserDetails to create an Authentication object
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Sets the current Authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
