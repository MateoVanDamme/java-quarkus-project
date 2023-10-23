# Developing a REST API using Quarkus

Quarkus supports JAX-RS annotations, allowing a declarative approach to registering endpoint handlers and mapping HTTP operations to Java code.

The class `TopicResource` in the module api-service, gives a good example of a typical HTTP Resource with support for the basic CRUD operations, implemented using Quarkus. The following sections are a summary of the basic principles.

## Compile dependencies

We've included the following dependencies via a common build script in `buildSrc/src/main/kotlin/sysdesign-service.gradle.kts` to be able to use the features described in the next sections:

```
implementation("io.quarkus:quarkus-resteasy-reactive")
implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
testImplementation("io.quarkus:quarkus-test-security")
testImplementation("io.rest-assured:rest-assured")
```

The `quarkus-test-security` and `rest-assured` libraries are not required for building or running the application, but are used for implementing the included unit tests.

## Registering a class as an HTTP Resource

Annotate a class with `@Path` to allow Quarkus to register it as an HTTP Resource implementation. For example:

```java
@Path("/hello")
public class Endpoint {

    @GET
    public String hello() {
        return "Hello, World!";
    }
}
```

This class results in an HTTP endpoint that will return "Hello, World!" when sending a GET request to the path `/hello`.
Notice how the `hello()` method returns a plain String instance as a return type. We can get away with this in this example because the implementation can return directly without blocking the request thread. When the implementation requires I/O (e.g. calling a database or external service), the method should return a `Uni` or `Multi` (see [Introduction to Mutiny](./mutiny-introduction.md)):

```java
@Path("/hello")
public class Endpoint {

    @Inject
    ExampleInterface someExternalService;
    
    @GET
    public Uni<String> hello() {
        return someExternalService.performHello();
    }
}
```

## Writing handlers for specific HTTP operations

Annotate your method with `@POST`, `@GET`, `@PUT`, `@DELETE`, etc, to indicate which HTTP method you are handling.
In addition, you can specify the Content-Type that is accepted (using the `@Consumes` annotation) and the Content-Type of the operation output (using the `@Produces` annotation).
Quarkus performs automatic content negotiation based on these annotations. E.g. a request with content-type `text/plain` will not be accepted by the following example, and will result in an `HTTP 406 - Not Acceptable` error response.

```java
@Path("/items")
public class Endpoint {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createItem(Item item) {
        return Item.persist(item).map(createdItem -> Response.created(buildUri(createdItem)).build());
    }
}
```

Notice the use of the Response builder to generate a custom HTTP response. Use this API e.g. to set response headers, perform redirects, etc.

## Handling path or query parameters

Using the `@PathParam` and `@QueryParam` annotations, Quarkus can inject path or query parameters as method arguments. For example:

```java
@Path("/items")
public class Endpoint {
    
    @Path("{itemId}/children")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Item> listItemChildren(@PathParam("itemId") String itemId, @QueryParam("filter") String filter) {
        var filterDocument = and(eq("parentId", itemId), parseFilter(filter));
        return ItemChild.find(filterDocument).stream();
    }
}
```

Notice the use of `@DefaultValue` to specify a default value for query parameters. Query parameters that have a default value, can be omitted from the HTTP request (without a default value, not supplying the query parameter would result in a bad request).

## Reference

For a complete overview of the Quarkus REST service capabilities, we refer to https://quarkus.io/guides/resteasy-reactive.
