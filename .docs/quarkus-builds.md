# Building and deploying Quarkus-based Micro-services

## Dev, Test and Production profiles

It is important to know that Quarkus comes with three built-in execution profiles:

* `dev`: used for running the application in development mode. Certain features (such as the development UI hosted at http://localhost:8080/q/dev) are enabled by default and [Dev Services](https://quarkus.io/guides/dev-services) can be used for dependencies that rely on external technologies (such as Keycloak, Kafka). This profile is automatically activated when running the application from your IDE, or using the `quarkusDev` Gradle goal.
* `test`: used for running the application in test mode. This mode enables the same development utilities as `dev`-mode, but allows having some test-specific settings. This profile is automatically activated when the JUnit framework is executed from within the context of a Quarkus module.
* `prod`: used for running the application in production mode. This mode disables all development utilities by default, only activating the essential framework components, optimizing the runtime for security and performance. This profile is automatically activated when starting the application from a Docker image built via Quarkus.

You can customize settings for each of these profiles in the `application.properties` file of a module (located in the folder `src/main/resources`) by prefixing a configuration property with `%{profile}`. For example, say you would want to use a different MongoDB database (configured using the property `quarkus.mongodb.database`) in production vs. development time:

```properties
%dev.quarkus.mongodb.database=development-db
%prod.quarkus.mongodb.database=production-db
```

If no specific configuration is found for the `test`-profile, the framework will default to `dev`-profile settings. That is why there is no explicit entry for `%test` in the above example. To read more about Quarkus profiles: https://quarkus.io/guides/config-reference#profiles

## Building Docker images

Quarkus supports building Docker images using a Gradle command via [Google Jib](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin).
To enable this feature, the dependency `io.quarkus:quarkus-container-image-jib` must be added to the classpath. For the demonstration project, this dependency has already been included, via the common Quarkus-service Gradle configuration in `buildSrc/src/main/kotlin/sysdesign-service.gradle.kts`. 

To build Docker images, execute the following command:

```shell
gradle build -Dquarkus.container-image.build=true -Dquarkus.container-image.group=sysdesign -Dquarkus.container-image.additional-tags=latest
```

The configuration property `quarkus.container-image.group` overrides group name for the resulting images (which defaults to the username of the user executing the command), while the property `quarkus.container-image.additional-tags` is used to also tag the newly build image with `latest`.
You can check if the build was successful via `docker images`, which should return something similar to:

```shell
REPOSITORY                                                                     TAG                                                                          IMAGE ID       CREATED             SIZE
sysdesign/processor-service                                                    1.0.0-SNAPSHOT                                                               8e4a7b9b2308   2 minutes ago       409MB
sysdesign/processor-service                                                    latest                                                                       8e4a7b9b2308   2 minutes ago       409MB
sysdesign/monolith                                                             1.0.0-SNAPSHOT                                                               de2a1cb725c8   2 minutes ago       420MB
sysdesign/monolith                                                             latest                                                                       de2a1cb725c8   2 minutes ago       420MB
sysdesign/api-service                                                          1.0.0-SNAPSHOT                                                               6e40f0796ca0   2 minutes ago       419MB
sysdesign/api-service                                                          latest                                                                       6e40f0796ca0   2 minutes ago       419MB
```

## Creating a deployment

Quarkus features mechanisms for deploying to [Kubernetes](https://quarkus.io/guides/deploying-to-kubernetes) or [OpenShift](https://quarkus.io/guides/deploying-to-openshift) clusters or to commercial Cloud environments such as [Azure](https://quarkus.io/guides/deploying-to-azure-cloud), [Amazon](https://quarkus.io/guides/amazon-lambda-http) and [Google Cloud](https://quarkus.io/guides/deploying-to-google-cloud).
However, setting up a Cloud deployment can be challenging, and we prefer you to focus on the design and implementation of your project. If you want to run a basic build of your code in an execution environment that supports persistence, a simple solution can be to use Docker Compose.

The folder `.deployment` contains an example `docker-compose.yml` file for such a deployment. It defines a set of services consisting of the Quarkus Micro-services for which images were built in the previous section, and the external dependencies (MongoDB, Keycloak and Kafka) that are used.

As a bonus, we've included a [Traefik](https://doc.traefik.io/traefik/) instance as a reverse proxy, which can route HTTP requests to either the Demo `api-service` or the Keycloak instance, based on the HTTP Host header. Traefik is often used in Micro-service deployments to act as an API Gateway, making the different HTTP operations of each Micro-service available from a single API endpoint. This simplifies the deployment: only the API Gateway needs to be publicly available, and it can be used to handle common API requirements, such as taking care of SSL termination (for HTTPs support) or rate limiting.

To start the deployment, open a terminal, switch to the directory that contains the `docker-compose.yml` file and then execute `docker-compose up -d`. It may take a while before all services are initialized.

Keycloak will be accessible via http://keycloak.localhost

The Swagger UI for the Sysdesign `api-service` will be accessible via http://api.localhost/q/swagger-ui/

To stop the deployment, execute `docker-compose down` or `docker-compose down -v` (to also remove the storage, deleting all data written to Kafka, Keycloak and MongoDB).
