package com.example;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

@Component
public class LoggingWorker {

    @JobWorker(type = "do-nothing")
    public void doNothing() {
        System.getLogger("com.example").log(System.Logger.Level.DEBUG, "Do nothing called");
    }
}
