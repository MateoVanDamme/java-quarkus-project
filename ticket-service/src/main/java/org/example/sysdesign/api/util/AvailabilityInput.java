package org.example.sysdesign.api.util;

import java.util.Date;

import org.example.sysdesign.model.Availability;
import org.example.sysdesign.model.Ticket;

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
public record AvailabilityInput(Date date, Integer amount) {

    /**
     * Create a new Rating instance based on this RatingInput and a supplied author id and exhibitionItemId.
     *
     * @param author - Id of the author of the rating.
     * @param exhibitionItemId - Id of the item this rating is for
     * @return A Rating instance
     */
    public Availability createNewAvailability() {
        return new Availability(this.date, this.amount);
    }

    public Availability updateAvailability(Availability availability){
        return new Availability(this.date, availability.getAmount());
    }
}
