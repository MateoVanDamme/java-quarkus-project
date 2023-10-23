package org.example.sysdesign.api.util;

import org.example.sysdesign.model.Rating;

/**
 * When creating a new Rating, or updating a Rating, only the content and rating attributes can be
 * manipulated directly. The other attributes of Rating are system generated.
 *
 * This utility class represents the input for rating write operations and facilitates creating
 * or setting derived Rating instances.
 *
 * @param content - The content of the rating
 * @param rating - the rating that the user gave 
 */
public record RatingInput(String content, Integer rating) {

    /**
     * Create a new Rating instance based on this RatingInput and a supplied author id and exhibitionItemId.
     *
     * @param author - Id of the author of the rating.
     * @param exhibitionItemId - Id of the item this rating is for
     * @return A Rating instance
     */
    public Rating createNewRating(String author, String exhibitionItemId) {
        return new Rating(author, exhibitionItemId, this.content, this.rating);
    }

    /**
     * Update an existing Rating instance, based on the content of this Rating.
     *
     * @param existingRating - Existing Rating instance.
     * @return A copy of the Rating, with updated content and rating attributes.
     */
    public Rating updateRating(Rating existingRating) {
        return new Rating(existingRating.id, existingRating.getCreatedAt(), existingRating.getAuthor(), existingRating.getPaintingId(), this.content, this.rating);
    }
}
