package io.ensueno.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaMultithreadConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMultithreadConsumerApplication.class, args);
    }

}
