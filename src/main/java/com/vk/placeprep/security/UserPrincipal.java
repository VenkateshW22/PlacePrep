package com.vk.placeprep.security;

import com.vk.placeprep.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 * Represents the authenticated user's principal and provides core user information.
 * This class wraps the application's User entity and adapts it for Spring Security.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public class UserPrincipal implements UserDetails {
    /** The user's unique identifier */
    private final Long id;
    
    /** The user's email address (used as username) */
    private final String email;
    
    /** The user's hashed password */
    private final String password;
    
    /** The user's granted authorities/roles */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a new UserPrincipal with the specified details.
     *
     * @param id The user's ID
     * @param email The user's email address
     * @param password The user's hashed password
     * @param authorities The user's granted authorities/roles
     */
    public UserPrincipal(Long id, String email, String password, 
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Creates a UserPrincipal from a User entity.
     *
     * @param user The user entity to create a UserPrincipal from
     * @return A new UserPrincipal instance
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(user.getRole().name())
        );
        return new UserPrincipal(
            user.getId(), 
            user.getUniversityEmail(), 
            user.getPassword(), 
            authorities
        );
    }

    /**
     * Returns the user's ID.
     *
     * @return The user's ID
     */
    public Long getId() { 
        return id; 
    }

    /**
     * Returns the username used to authenticate the user.
     * In this implementation, it's the user's email address.
     *
     * @return The username (email)
     */
    @Override
    public String getUsername() { 
        return email; 
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The password (hashed)
     */
    @Override
    public String getPassword() { 
        return password; 
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return The authorities, or an empty collection if none
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return authorities; 
    }

    // Implement other UserDetails methods with default values
    
    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is valid (non-expired), false otherwise
     */
    @Override 
    public boolean isAccountNonExpired() { 
        return true; 
    }
    
    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked, false otherwise
     */
    @Override 
    public boolean isAccountNonLocked() { 
        return true; 
    }
    
    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the user's credentials are valid (non-expired), false otherwise
     */
    @Override 
    public boolean isCredentialsNonExpired() { 
        return true; 
    }
    
    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override 
    public boolean isEnabled() { 
        return true; 
    }
}
