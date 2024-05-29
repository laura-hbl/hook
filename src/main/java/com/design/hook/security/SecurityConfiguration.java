package com.design.hook.security;

import com.design.hook.dto.UserDTO;
import com.design.hook.repository.IUserRepository;
import com.design.hook.security.jwt.JwtUtils;
import com.design.hook.util.DTOConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


/**
 * Application security configuration class.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);

    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final DTOConverter dtoConverter;
    public UserDetailsService userDetailsService;


    @Autowired
    public SecurityConfiguration(final UserDetailsService userDetailsService, final ObjectMapper objectMapper,
                                 final JwtUtils jwtUtils, final IUserRepository userRepository,
                                 final DTOConverter dtoConverter) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Allows configuring web based security for specific http requests.
     *
     * @param http HttpSecurity
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/patterns/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                );
/*                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(this::loginSuccessHandler)
                        .failureHandler(this::loginFailureHandler)
                )
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(this::logoutSuccessHandler)
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this::authenticationEntryPointHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);*/
        return http.build();


    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Creates an instance of BCryptPasswordEncoder in order to encrypt the password.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
  /*      config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);*/
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    /**
     * Handles authentication success. A token is created, then added in a cookie which will be secure.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void loginSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final Authentication authentication) throws IOException {

        // Generates a json web token
        String bearer = "Bearer " + jwtUtils.generateJwtToken(authentication);
        response.addHeader(HttpHeaders.AUTHORIZATION, bearer);
        response.setStatus(HttpStatus.OK.value());

        var userLoggedIn = userRepository.findByUserName(authentication.getName());
        UserDTO userDTO = dtoConverter.toUserDTO(userLoggedIn.get());

        ObjectMapper map = new ObjectMapper();
        JSONObject json = new JSONObject();
        json.appendField("token", bearer.substring(7));
        json.appendField("user", userDTO);
        PrintWriter out = response.getWriter();

        map.writeValue(out, json);
    }

    /**
     * Handles login failure.
     *
     * @param response HttpServletResponse object
     * @param request  HttpServletRequest object
     * @param e        AuthenticationException object
     */
    private void loginFailureHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final AuthenticationException e) throws IOException {

        LOGGER.info("onLoginFailureHandler", e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        objectMapper.writeValue(response.getWriter(), "The username or password you entered is incorrect.");
    }

    /**
     * Handles logout success.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void logoutSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                      final Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "You have successfully logged out!");
    }

    /**
     * Handles authenticationEntryPoint.
     *
     * @param response HttpServletResponse object
     * @param request  HttpServletRequest object
     * @param e        AuthenticationException object
     */
    private void authenticationEntryPointHandler(final HttpServletRequest request, final HttpServletResponse response,
                                                 final AuthenticationException e) throws IOException {

        response.sendError(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), "Access Denied");
    }

    /**
     * Custom AuthTokenFilter class that extends OncePerRequestFilter and overrides doFilterInternal() method.
     * Filter that executes once per request.
     */
    @Component
    public static class AuthTokenFilter extends OncePerRequestFilter {

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
}
