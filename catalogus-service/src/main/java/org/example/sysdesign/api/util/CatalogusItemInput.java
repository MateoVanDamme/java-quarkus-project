package org.example.sysdesign.api.util;

import org.example.sysdesign.model.CatalogusItem;

/**
 * When creating a new CatalogusItem, or updating a CatalogusItem, some attributes are system generated. (id)
 *
 * This utility class represents the input for CatalogusItem write operations and facilitates creating
 * or setting derived CatalogusItem instances.
 * @param name - The name of the catalogus item.
 * @param artist - The artist of the catalogus item.
 * @param description - The description of the catalogus item.
 * @param style - The style of the catalogus item.
 * @param exhibitionID - The exhibitionID of the catalogus item.
 * @param location - The location of the catalogus item.
 *
 */
public record CatalogusItemInput(String name, String artist, String description, String style, String exhibitionID, String location) {

    /**
     * Create a new CatalogusItem instance based on this CatalogusItemInput with following parameters.
     */
    public CatalogusItem createNewCatalogusItem() {
        return new CatalogusItem(this.name, this.artist, this.description, this.style, this.exhibitionID, this.location);
    }

    /**
     * Update an existing CatalogusItem instance, based on the content of this CatalogusItem.
     * @param existingCatalogusItem - The existing CatalogusItem that needs to be updated.
     */
    public CatalogusItem updateCatalogusItem(CatalogusItem existingCatalogusItem) {
        return new CatalogusItem(existingCatalogusItem.id, 
                                    this.name,
                                    this.artist,
                                    this.description,
                                    this.style,
                                    this.exhibitionID,
                                    this.location);
    }


}