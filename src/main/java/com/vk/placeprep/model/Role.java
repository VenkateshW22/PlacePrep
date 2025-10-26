package com.vk.placeprep.model;

/**
 * Represents the different user roles in the application.
 * Used for authorization and access control throughout the system.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public enum Role {
    /**
     * Standard user with basic access rights.
     * Can view and submit interview experiences, update their profile, etc.
     */
    USER,
    
    /**
     * Administrative user with elevated privileges.
     * Can manage users, moderate content, and access administrative features.
     */
    ADMIN
}
