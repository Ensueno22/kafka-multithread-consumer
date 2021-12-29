package io.ensueno.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class PushServerActivator {

    private static List<ConsumerWorker> workerThreads = new ArrayList<>();

    ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute() throws Exception {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        createServerQueue();
    }

    private void createServerQueue() {
        createConsumerWorkers(customProps(), 3, "test");
    }

    private Properties customProps() {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        return configs;
    }

    private void createConsumerWorkers(Properties props, int workerCount, String topic) {
        for(int i=0; i<workerCount; i++){
            ConsumerWorker worker = new ConsumerWorker(props, topic, workerCount);
            workerThreads.add(worker);
            executorService.execute(worker);
        }
    }

    static class ShutdownThread extends Thread {
        public void run() {
            workerThreads.forEach(ConsumerWorker::shutdown);
        }
    }

}
