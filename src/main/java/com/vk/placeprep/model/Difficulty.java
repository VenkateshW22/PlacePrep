package com.vk.placeprep.model;

/**
 * Represents the difficulty level of an interview round or question.
 * Used to categorize and filter interview experiences and preparation materials.
 * 
 * @author Venkatesh K
 * @version 1.0
 * @since 2025-10-26
 */
public enum Difficulty {
    /**
     * Easy difficulty level.
     * Indicates questions or rounds that are straightforward and require basic knowledge.
     * Typically covers fundamental concepts and is suitable for beginners.
     */
    EASY, 
    
    /**
     * Medium difficulty level.
     * Indicates questions or rounds that require a good understanding of concepts.
     * May involve combining multiple concepts or applying them in slightly complex scenarios.
     */
    MEDIUM, 
    
    /**
     * Hard difficulty level.
     * Indicates challenging questions or rounds that require advanced knowledge.
     * Often involves complex problem-solving, optimization, or system design.
     */
    HARD
}
