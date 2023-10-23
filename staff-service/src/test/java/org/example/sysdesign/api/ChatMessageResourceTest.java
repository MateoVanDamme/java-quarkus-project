package org.example.sysdesign.api;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.vertx.core.json.Json;
import org.example.sysdesign.model.ChatMessage;
import org.example.sysdesign.model.Rating;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Enable the JUnit Quarkus extension for this test.
@QuarkusTest
// The Quarkus extension allows setting an HTTP Resource as the root target for
// the test.
@TestHTTPEndpoint(StaffmemberResource.class)
public class ChatMessageResourceTest {

    // // Use @Test to register a method as a JUnit test case.
    // @Test
    // // @TestSecurity enables mocking a specific user and/or set of roles for the
    // HTTP interactions in the test.
    // @TestSecurity(user = "test-author")
    // public void testStreamMessages() throws URISyntaxException, IOException,
    // InterruptedException {
    // // Create a test topic
    // var topic = new Topic("test-author", "test", null);
    // Topic.persist(topic).await().indefinitely();

    // // Post a number of test messages
    // for(int i = 0; i < 15; i++) {
    // given()
    // .contentType(ContentType.TEXT)
    // .body("Message " + i)
    // .when()
    // .post("{id}/messages", topic.id.toString())
    // .then()
    // .statusCode(204);
    // }

    // // Start a stream reading the messages
    // var uri = new URI("http://localhost:8081/topics/" + topic.id + "/messages");
    // var executor = Executors.newSingleThreadExecutor();
    // var client = HttpClient.newBuilder().executor(executor).build();
    // var request = HttpRequest.newBuilder(uri).GET().build();
    // var lines = client.send(request, HttpResponse.BodyHandlers.ofLines()).body();
    // var messages = lines
    // .filter(line -> line.startsWith("data:"))
    // .map(line -> Json.decodeValue(line.replace("data:", ""), ChatMessage.class))
    // .limit(15).toList();
    // assertEquals(IntStream.range(0, 15).mapToObj(i -> "Message " + i).toList(),
    // messages.stream().map(ChatMessage::content).toList());
    // executor.shutdownNow();

    // // Cleanup the test topic
    // Topic.deleteById(topic.id).await().indefinitely();
    // }

}
