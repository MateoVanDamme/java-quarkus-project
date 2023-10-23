package org.example.sysdesign.monolith;

import io.quarkus.runtime.Quarkus;

/**
 * Utility class for launching the monolith.
 *
 * Note that Quarkus does not require a Launcher, but having one makes it easier to run the service from your IDE.
 */
public class Launcher {

    public static void main(String ... args) {
        System.out.println("Starting all services as a monolith for dev purposes...");
        Quarkus.run(args);
    }
}
