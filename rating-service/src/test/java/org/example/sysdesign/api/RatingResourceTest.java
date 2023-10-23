package org.example.sysdesign.api;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.core.HttpHeaders;

import org.example.sysdesign.api.util.PagedRatingResult;
import org.example.sysdesign.api.util.Roles;
import org.example.sysdesign.api.util.RatingInput;
import org.example.sysdesign.model.Rating;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

// Enable the JUnit Quarkus extension for this test.
@QuarkusTest
// The Quarkus extension allows setting an HTTP Resource as the root target for
// the test.
@TestHTTPEndpoint(RatingResource.class)
public class RatingResourceTest {

    private static final String TEST_AUTHOR = "test-author";

    // @Test
    // // @TestSecurity enables mocking a specific user and/or set of roles for the
    // HTTP interactions in the test.
    // @TestSecurity(user = TEST_AUTHOR)
    // public void testCreateAndGet() {
    // var timestamp = Instant.now();
    // var input = new TopicInput("test", "This is a test topic!");

    // // Create a new entity by performing an HTTP POST
    // var entityLocation = given()
    // .contentType(ContentType.JSON)
    // .body(input)
    // .when()
    // .post()
    // .then()
    // .statusCode(201)
    // .extract().header(HttpHeaders.LOCATION);

    // // Check if the created entity can be retrieved using the Location header of
    // the POST HTTP response.
    // var createdInstance = when()
    // .get(entityLocation)
    // .then()
    // .statusCode(200)
    // .extract().as(Topic.class);

    // // Check if the retrieved instance matches the created instance.
    // assertNotNull(createdInstance.id);
    // assertTrue(createdInstance.getCreatedAt().isAfter(timestamp) &&
    // createdInstance.getCreatedAt().isBefore(Instant.now()));
    // assertEquals(TEST_AUTHOR, createdInstance.getAuthor());
    // assertEquals(input.name(), createdInstance.getName());
    // assertEquals(input.description(), createdInstance.getDescription());
    // }

    // @Test
    // public void testList() {
    // // Clear existing instances (using blocking call, as test code is not
    // performance critical)
    // Topic.deleteAll().await().indefinitely();

    // // Create test instances
    // var topics = IntStream.range(0, 100)
    // .mapToObj(i -> new Topic(TEST_AUTHOR, "test-topic-" + i, "This is test topic
    // #" + i))
    // .collect(Collectors.toList());

    // // Persist test instances (using blocking call, as test code is not
    // performance critical)
    // Topic.persist(topics).await().indefinitely();

    // var pageIndex = 0;
    // var hasNextPage = true;
    // var topicResults = new LinkedList<Topic>();
    // do {
    // var topicPage = given()
    // .queryParam("pageIndex", pageIndex++)
    // .when()
    // .get()
    // .then()
    // .statusCode(200)
    // .extract().as(PagedTopicResult.class);
    // topicResults.addAll(topicPage.topics());
    // hasNextPage = topicPage.next();
    // } while (hasNextPage);

    // assertEquals(topics, topicResults);
    // }

    // @Test
    // // Here @TestSecurity is used to mock a user that has the ADMIN role.
    // @TestSecurity(user = TEST_AUTHOR, roles = Roles.ADMIN)
    // public void testUpdate() {
    // // Persist test instance
    // var topic = new Topic(TEST_AUTHOR, "test-topic-update", "This si a test topic
    // used for testing updates.");
    // Topic.persist(topic).await().indefinitely();

    // // Update the typo
    // var correctDescription = "This is a test topic used for testing updates.";
    // given()
    // .contentType(ContentType.JSON)
    // .body(new TopicInput(topic.getName(), correctDescription))
    // .when()
    // .put("{id}", topic.id.toString())
    // .then()
    // .statusCode(204);

    // // Check if the update succeeded
    // var updatedTopic = Topic.<Topic>findById(topic.id).await().indefinitely();
    // assertEquals(correctDescription, updatedTopic.getDescription());
    // }

    // @Test
    // @TestSecurity(user = TEST_AUTHOR, roles = Roles.ADMIN)
    // public void testDelete() {
    // // Persist test instance
    // var topic = new Topic(TEST_AUTHOR, "test-topic-delete", "This is a test
    // topic!");
    // Topic.persist(topic).await().indefinitely();

    // when()
    // .delete("{id}", topic.id.toString())
    // .then()
    // .statusCode(204);

    // // Check if the topic has been deleted
    // var topicOptional =
    // Topic.findByIdOptional(topic.id.toString()).await().indefinitely();
    // assertTrue(topicOptional.isEmpty());
    // }

}
