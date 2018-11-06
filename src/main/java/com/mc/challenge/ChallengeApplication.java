package com.mc.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author M.Kubendranathan
 *
 * Spring Boot application class to check if 2 points are connected in a given map
 */
@SpringBootApplication
@EnableCaching
public class ChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }
}
