# Interfacing with Kafka using Quarkus

## What is Apache Kafka?

[Apache Kafka](https://kafka.apache.org) is a mature open-source stream-processing software for collecting, processing, storing, and analyzing data at scale. Most known for its excellent performance, low latency, fault tolerance, and high throughput, it's capable of handling thousands of messages per second. Kafka is a proven technology that is used in production by numerous companies worldwide (see: https://kafka.apache.org/powered-by).

Thanks to its reliability and flexibility, Kafka can be a very useful enabling technology for realizing your System Design project. Some examples of scenarios in which Kafka could be used are:

* As a **message broker** in a Micro-service architecture: use Kafka as a publish/subscribe service to reliably implement communication between your Micro-services. The consumer group mechanism of Kafka allows having control of how messages are delivered in case of horizontally scaled Micro-services (e.g. when having multiple instances of a processing-service, only one of these instances should react to an event).
* As a **buffering queue**: in large-scale IoT or Industry 4.0 applications, connecting the API for incoming data directly to your storage is a recipe for disaster. Peaks in data load can easily overload the database, also preventing queries from being executed. By writing incoming data to Kafka instead, you can buffer peak loads and let the storage catch up at its own pace.
* As backend for **event-sourcing**: use Kafka to log state changes as a time-ordered sequence of records. Kafka's support for very large log data makes it an excellent backend for an application built in this style.
* As **stream processing** platform: Kafka provides all the necessary tools to implement robust and scalable stream processing and a high level API dedicated to stream processing is available in the form of [Kafka Streams](https://kafka.apache.org/documentation/streams/).

## Compile dependencies

We've included the following dependencies via a common build script in `buildSrc/src/main/kotlin/sysdesign-service.gradle.kts` to be able to use the features described in the next sections:

```
implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
```

## Quarkus Reactive Messaging

Quarkus allows you to interact with Kafka through an implementation of the [Eclipse Reactive Messaging specification](https://github.com/eclipse/microprofile-reactive-messaging). This specification introduces a number of high-level abstractions and makes it easier for you to get started using Kafka in a reactive system. Like the other Quarkus features we've covered, this reactive messaging is driven by annotations.

The Reactive Messaging specification is based around the abstraction of a **channel**. This can be seen as a technology-agnostic way of representing a specific flow of events. For Kafka the concept of a channel can be mapped to that of a **topic**.

### Producing messages

The following example shows how you can post new messages to a channel from an HTTP operation handler:

```java
@Path("/prices")
public class PriceResource {

    @Inject
    @Channel("price-out")
    MutinyEmitter<Double> priceEmitter;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<String> addPrice(Double price) {
        return quoteRequestEmitter.send(price)
                .map(x -> "ok")
                .onFailure().recoverWithItem("ko");
    }
}
```

To produce message, inject an `Emitter` from the Quarkus CDI context. In this case we use the `MutinyEmitter` variant, so we can implement the HTTP operation in a non-blocking way using Mutiny. The `@Channel` specifies to what channel the message should be produced.

### Consuming messages

The following example shows how you can consume messages from a channel and stream these over HTTP using Server-Sent-Events:

```java
@Path("/prices")
public class PriceResource {

    @Inject
    @Channel("prices-in")
    Multi<Double> prices;

    @GET
    @Path("/prices")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public Multi<Double> stream() {
        return prices;
    }
}
```

The `Multi` representing the stream of messages is injected from the CDI context. The `@Channel` annotation is used to specify from what channel the messages should be consumed.

### Message processing

The following example shows how you can register a message processing handler. All incoming prices are converted using a preset rate and then sent to another channel:

```java
@ApplicationScoped
public class PriceProcessor {

    private static final double CONVERSION_RATE = 0.88;

    @Incoming("price-in")
    @Outgoing("price-out")
    public Multi<Double> process(Multi<Integer> prices) {
        return prices.filter(p -> p > 100).map(p -> p * CONVERSION_RATE);
    }

}
```

Both the incoming and outgoing channels for the message processing handler method are configured using the respective annotations. Do not forget to register the class with Quarkus CDI using the `@ApplicationScoped` anotation (otherwise processing will not be executed). Both the incoming and outgoing message streams are represented as a `Multi` instance.

## Configuring Quarkus to use Kafka

The Reactive Messaging specification provides us with a high level, technology-agnostic abstraction on how messaging can be implemented using the constructs of channels. To effectively use Kafka as a backend for messaging, explicit bindings need to be configured in the application configuration.

```properties
# Configure the 'messages-out' channel to produce to the Kafka 'chat-messages' topic.
mp.messaging.outgoing.messages-out.topic=chat-messages
mp.messaging.outgoing.messages-out.connector=smallrye-kafka
mp.messaging.outgoing.messages-out.merge=true

# Configure the 'message-in' channel to consume from the Kafka 'chat-messages' topic.
mp.messaging.incoming.messages-in.topic=chat-messages
mp.messaging.incoming.messages-in.connector=smallrye-kafka
mp.messaging.incoming.messages-in.auto.offset.reset=earliest
mp.messaging.incoming.messages-in.broadcast=true
```

Notice how Reactive Messaging differentiates between incoming and outgoing channels. This is different from a Kafka topic, which supports both producers and consumers. This explains the above configuration: both the outgoing 'messages-out' channel and the incoming 'messages-in' channel are bound to the same Kafka 'chat-messages' topic. The `mp.messaging.incoming.[channel].connector` property specifies what connector to use for the configuration (this binds a channel to a specific backend technology, in this case Kafka).

Some additional properties are used in this example:
* `mp.messaging.outgoing.messages-out.merge=true`: this setting allows the channel 'messages-out' to be used as an outgoing channel by more than one emitter (default is false). The messages coming from the various emitters are then merged into a single flow.
* `mp.messaging.incoming.messages-in.auto.offset.reset=earliest`: by default a Kafka consumer will ignore older messages when its consumer state is reset, or is not present, e.g. in case of a new consumer. By setting this setting to `earliest` (instead of the default `latest`), we make sure that the consumer receives the full backlog of messages.
* `mp.messaging.incoming.messages-in.broadcast=true`: if broadcast is set to `true`, the produced messages are broadcast to multiple consumers. If set to false, only the consumer that is discovered first by the Quarkus framework will receive the event. Setting this setting to `true` for incoming channels makes more sense when using Kafka as a backend technology.

## References

For a complete overview of the Quarkus Reactive Messaging through Kafka, we refer to https://quarkus.io/guides/kafka.
