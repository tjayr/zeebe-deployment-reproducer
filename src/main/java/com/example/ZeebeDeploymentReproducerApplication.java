package com.example;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = {"classpath*:**/*.bpmn", "classpath*:**/*.dmn"})
@EnableZeebeClient
public class ZeebeDeploymentReproducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeebeDeploymentReproducerApplication.class, args);
    }

}
