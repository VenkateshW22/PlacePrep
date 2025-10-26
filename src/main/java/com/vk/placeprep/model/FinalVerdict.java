package com.vk.placeprep.model;

/**
 * Represents the final outcome or status of a job application or interview process.
 * This enum is used to track the ultimate result of a candidate's application.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public enum FinalVerdict {
    /**
     * The candidate was selected for the position.
     */
    SELECTED,
    
    /**
     * The candidate was not selected for the position.
     */
    REJECTED,
    
    /**
     * The final decision is still pending.
     * The application is under review or in progress.
     */
    AWAITING,
    
    /**
     * The application is in an initial pending state.
     * This is typically the default state for new applications.
     */
    PENDING,
    
    /**
     * The candidate received and accepted an offer for the position.
     * This is a final state indicating successful completion of the hiring process.
     */
    OFFER_ACCEPTED
}
