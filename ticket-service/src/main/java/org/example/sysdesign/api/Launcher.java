package org.example.sysdesign.api;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Utility class for launching the api-service.
 *
 * Note that Quarkus does not require a Launcher, but having one makes it easier to run the service from your IDE.
 */
public class Launcher {

    public static void main(String ... args) {
        System.out.println("Starting the ticket-service...");
        Quarkus.run(args);
    }
}
