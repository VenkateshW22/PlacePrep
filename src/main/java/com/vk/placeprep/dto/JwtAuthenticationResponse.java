package com.vk.placeprep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object representing the response for JWT authentication.
 * Contains the JWT access token and token type for authenticated users.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    /**
     * The JWT access token to be used for subsequent authenticated requests.
     * This token should be included in the Authorization header of HTTP requests.
     */
    private String accessToken;
    
    /**
     * The type of the token, which is always "Bearer" for JWT tokens.
     * This follows the OAuth 2.0 Bearer Token specification.
     */
    private String tokenType = "Bearer";

    /**
     * Constructs a new JwtAuthenticationResponse with the specified access token.
     * The token type will be set to "Bearer" by default.
     *
     * @param accessToken The JWT access token
     */
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
