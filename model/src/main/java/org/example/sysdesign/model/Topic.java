package org.example.sysdesign.model;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Class for representing a Chat Topic (not to be confused with a Kafka topic!)
 * It extends ReactivePanacheMongoEntity, making this entity an
 * <a href="https://quarkus.io/guides/mongodb-panache#solution-1-using-the-active-record-pattern">Active Record </a>.
 * <p>
 * Ideally, the storage concerns would not be tied to the model definition, as this prevents easily switching between
 * storage implementations. However, this approach serves this demonstrator for the sake of simplicity.
 */
@RegisterForReflection
public class Topic extends ReactivePanacheMongoEntity {

    private Instant createdAt;

    private String author;

    private String name;

    private String description;

    public Topic() {
    }

    /**
     * Create a new Chat Topic.
     *
     * @param id          - Id for the topic (should be generated by the storage driver).
     * @param createdAt   - Timestamp at which the topic is created.
     * @param author      - Id of the author of the topic.
     * @param name        - Name of the topic.
     * @param description - Description of the purpose of the topic.
     */
    public Topic(ObjectId id, Instant createdAt, String author, String name, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.author = author;
        this.name = name;
        this.description = description;
    }

    /**
     * Create a new Chat Topic.
     *
     * @param author      - Id of the author of the topic.
     * @param name        - Name of the topic.
     * @param description - Description of the purpose of the topic.
     */
    public Topic(String author, String name, String description) {
        this(null, Instant.now().truncatedTo(ChronoUnit.MILLIS), author, name, description);
    }

    /**
     * Get the timestamp at which the topic was created.
     *
     * @return an instance of Instant
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the timestamp at which the topic was created.
     *
     * @param createdAt - Timestamp at which the topic was created.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the id of the author of the topic.
     *
     * @return a String instance.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the id of the author the topic.
     *
     * @param author - Id of the author of the topic.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the name of the topic.
     *
     * @return a String instance
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the topic.
     *
     * @param name - The name of the topic.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the topic.
     *
     * @return a String instance
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the topic.
     *
     * @param description - The description of the topic.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(id, topic.id) && createdAt.equals(topic.createdAt) && author.equals(topic.author) && name.equals(topic.name) && Objects.equals(description, topic.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, author, name, description);
    }
}