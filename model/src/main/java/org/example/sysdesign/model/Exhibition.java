package org.example.sysdesign.model;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;
/**
 * Class for representing the exhibitions of a museum.
 * An exhibition stores its start and end date, the name of the exhibition, a description and an id.
 */
@RegisterForReflection
public class Exhibition extends ReactivePanacheMongoEntity{

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;


    // create constructors
    public Exhibition() {
    }

    /**
     * Create a new exhibition item
     * 
     * @param id                 - Id for the exhibition.
     * @param name               - name of the exhibition.
     * @param description        - description of the exhibition.
     * @param startDate          - start date of the exhibition.
     * @param endDate            - end date of the exhibition.
     * 
     */
    public Exhibition(ObjectId id, String name, String description, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Create a new exhibition item, without giving the ID, the ID will be generated by the system
     * 
     * @param name               - name of the exhibition.
     * @param description        - description of the exhibition.
     * @param startDate          - start date of the exhibition.
     * @param endDate            - end date of the exhibition.
     * 
     */
    public Exhibition(String name, String description, Date startDate, Date endDate){
        this(null, name, description, startDate, endDate);
    }

    /**
     * Get the name of the exhibition
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the exhibition
     * @param name - name of the exhibition
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the exhibition
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set the description of the exhibition
     * @param description - description of the exhibition
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the start date of the exhibition
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Set the start date of the exhibition
     * @param startDate - start date of the exhibition
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the end date of the exhibition
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Set the end date of the exhibition
     * @param endDate - end date of the exhibition
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Function to compare two exhibitions
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Exhibition)) {
            return false;
        }
        Exhibition exhibition = (Exhibition) o;
        return Objects.equals(id, exhibition.id) && Objects.equals(name, exhibition.name) && Objects.equals(description, exhibition.description) && Objects.equals(startDate, exhibition.startDate) && Objects.equals(endDate, exhibition.endDate);
    }

    /**
     * Function to generate a hashcode for an exhibition
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate);
    }
    
    
}
