package org.example.sysdesign.api.util;

import org.example.sysdesign.model.Exhibition;
import java.util.Date;

/**
 * When creating a new Exhibition, or updating a Exhibition, some attributes are system generated.
 *
 * This utility class represents the input for Exhibition write operations and facilitates creating
 * or setting derived Exhibition instances.
 * @param name - Name of the exhibition.
 * @param description - Description of the exhibition.
 * @param startDate - Start date of the exhibition.
 * @param endDate - End date of the exhibition.
 *
 */
public record ExhibitionInput(String name, String description, Date startDate, Date endDate) {

    /**
     * Create a new Exhibition instance based on this ExhibitionInput with following parameters.
     */
    public Exhibition createNewExhibition() {
        return new Exhibition(this.name, this.description, this.startDate, this.endDate);
    }

    /**
     * Update an existing Exhibition instance, based on the content of this Exhibition.
     * @param existingExhibition - The existing Exhibition that needs to be updated.
     */
    public Exhibition updateExhibition(Exhibition existingExhibition) {
        return new Exhibition(existingExhibition.id, 
                                    this.name,
                                    this.description,
                                    this.startDate,
                                    this.endDate);
    }
}