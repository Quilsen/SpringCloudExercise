package org.example.internalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class InternalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternalServiceApplication.class, args);
    }

}
