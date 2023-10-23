# Introduction to Mutiny

Mutiny is a Java programming library that facilitates dealing with event-driven systems, asynchronous programming, non-blocking I/O, etc; by providing a feature-rich reactive API.
It is a relatively new library that incorporates lessons learned from earlier reactive programming implementations such as [RxJava](https://github.com/ReactiveX/RxJava).

## Why use Mutiny?

Legacy Java applcations block the active thread when dealing with I/O (writing to disk, sending packets over the network, etc...) and older Java HTTP server implementations commonly used a thread-per-request model.
As a result, applications coded using the above practices had limited scalability and typically required carefully tuned, powerful hardware.

The rise of [Node.js](https://nodejs.org/en/) resulted in the reactor pattern becoming mainstream. In this pattern, a single event loop thread is used to handle multiple incoming requests. This can only work when the request handling logic (including I/O operations) never blocks its thread. This was typically implemented using callbacks. Take the following [Vert.x](https://vertx.io) example:

```java
// Callback based HTTP operation handler
router.get("/person/:id/friends").handler(ctx -> {
    var personId = ctx.pathParam("id");
    // Fetch the Person record from the database
    storage.findPersonById(ctx.pathParam("id"), findPersonResult -> {
        if (findPersonResult.failed()) {
            // Log and write the error to the HTTP response
            Log.warn("An error occurred while fetching a Person record.", findPersonResult.cause());
            writeHttpError(ctx, findPersonResult.cause());
        } else {
            // DB lookup succeeded, proceed by fetching friend records by iterating over friend ids
            var person = findPersonResult.result();
            var output = new HashSet<Person>();
            person.getFriendIds().forEach(friendId -> storage.findPersonById(friendId, findFriendResult -> {
                if (findFriendResult.failed()) {
                    // Log and write the error to the HTTP response
                    Log.warn("An error occurred while fetching a Person record.", findFriendResult.cause());
                    writeHttpError(ctx, findFriendResult.cause());
                } else {
                    output.add(findFriendResult.result());
                    if (output.size() == person.getFriendIds().size()) {
                        // All friends were loaded, write as JSON to the HTTP response
                        ctx.json(output);
                    }
                }
            }));
        }
    });
});
```

Although the above code can easily scale up to thousands of users, one could argue that code like this is not easy to read and difficult to maintain. This issue is commonly referred to as "Callback Hell".
Along the years, several attempts were made to improve upon this situation. Developers were seeking to achieve the performance of asynchronous, callback-based code, while having the readability of blocking sequential code, or even going beyond this using functional and Flow-based paradigms. Examples of resulting solutions are:

* [Promises](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Using_promises) in Javascript/Typescript and similar solutions in the Java world such as [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-basics.html) and [Project Loom](https://openjdk.org/projects/loom/).
* Reactive Programming initiatives, such as RxJava and ... **Mutiny**

Mutiny avoids nested callbacks by adhering to an intuitive Flow-based paradigm. The above example could be rewritten using Mutiny as follows:

```java
// Declarative HTTP operation handler
@GET
@Path("/persons/{id}")
public Multi<Person> getFriends(@PathParam("id") String personId) {
    // Fetch the Person record from the database
    return storage.findPersonById(personId)
            .onItem().transformToMulti(person ->
                    // DB lookup succeeded
                    // Return a Multi that transforms the friend ids in Uni instances of Person (resulting from findPersonById calls).
                    Multi.createFrom().iterable(person.getFriendIds())
                            .onItem().transformToUniAndMerge(friendId -> storage.findPersonById(friendId)))
            // If an error occurs, produce a log statement (the error is automatically propagated to the HTTP response)
            .onFailure().invoke(err -> Log.warn("An error occurred while fetching a Person record.", err));
}
```

As long as Project Loom is still an experimental feature of Java, Mutiny is probably the best way of implementing scalable, high-performing applications in standard Java. 
If it is possible to use Kotlin (an alternative JVM language), Kotlin Coroutines are a viable alternative.

## Basic usage

For more information on the basic usage of Mutiny, we refer to the excellent documentation on the Mutiny homepage.

Start with the brief explanation of the basic Mutiny types `Uni` and `Multi`: https://smallrye.io/smallrye-mutiny/1.7.0/reference/uni-and-multi/

Next, read how you can transform the items of a `Uni` or `Multi`. Transform operations can be simple mapping functions (cfr. `map` when using the Java Streams API), or functions that in turn result in a `Uni` or `Multi` (e.g. representing an asynchronous operation). This allows the Mutiny API to be used for complex compositions that would be very difficult to express using more traditional code.

* Transforming items: https://smallrye.io/smallrye-mutiny/1.7.0/tutorials/transforming-items/
* Transforming items asynchronously: https://smallrye.io/smallrye-mutiny/1.7.0/tutorials/transforming-items-asynchronously/

The [Guides](https://smallrye.io/smallrye-mutiny/1.7.0/guides/imperative-to-reactive/) section documents more advanced usage and can be interesting to browse through.
