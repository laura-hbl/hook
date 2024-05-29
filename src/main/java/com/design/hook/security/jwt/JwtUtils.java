package com.design.hook.security.jwt;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Permits to generate a JWT, validate a JWT or get username from JWT.
 */
@Component
public class JwtUtils {

    private static final Logger LOGGER = LogManager.getLogger(JwtUtils.class);

    /**
     * Jwt secret key.
     */
    @Value("${hook.app.jwtSecret}")
    private String jwtSecret;

    /**
     * Jwt expiration time in milliseconds.
     */
    @Value("${hook.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT from username, date, expiration and secret.
     *
     * @param authentication Authentication reference
     * @return The JWT generated
     */
    public String generateJwtToken(final Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    /**
     * Gets username from the given JWT.
     *
     * @param token the JWT
     * @return The username retrieved from the token
     */
    public String getUserNameFromJwtToken(final String token) {

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates a JWT by using the jwtSecret property to generate the signing key, and uses that to verify that
     * the JWT has not been tampered with.
     *
     * @param authToken the JWT to validate
     * @return True if the JWT is valid or false is not valid
     */
    public boolean validateJwtToken(final String authToken) {

        try {
            // This line will throw an exception if it is not a signed JWS (as expected)
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
    /*    } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());*/
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
