package io.ensueno.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class ConsumerWorker implements Runnable {

    private Properties props;
    private String topic;
    private String threadName;
    private KafkaConsumer<String, byte[]> consumer;

    public ConsumerWorker(Properties props, String topic, int number) {
        this.props = props;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        try {
            while(true){
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, byte[]> record: records){
                    log.info("threadName=[{}], record value=[{}]", threadName, record.value());
                }
                consumer.commitSync();
            }
        } catch (WakeupException we){
            log.error("threadName=[{}] trigger WakeupException", threadName);
        } finally {
            consumer.commitSync();
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}
