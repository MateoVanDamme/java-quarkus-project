package org.example.sysdesign.api;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Utility class for launching the catalogus-service.
 *
 * 
 */
public class Launcher {

    public static void main(String ... args) {
        System.out.println("Starting the catalogus-service...");
        Quarkus.run(args);
    }
}
