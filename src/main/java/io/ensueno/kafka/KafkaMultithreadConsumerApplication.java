package io.ensueno.kafka;

import io.ensueno.kafka.consumer.PushServerActivator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaMultithreadConsumerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMultithreadConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        PushServerActivator pushServerActivator = new PushServerActivator();
        pushServerActivator.execute();
    }
}
