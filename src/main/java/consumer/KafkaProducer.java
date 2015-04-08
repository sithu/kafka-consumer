package consumer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by saung on 4/7/15.
 */

public class KafkaProducer {
    private static Producer<Integer, String> producer;
    private final Properties properties = new Properties();

    public KafkaProducer(String kafkaURL) {
        properties.put("metadata.broker.list", kafkaURL);
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<>(new ProducerConfig(properties));
    }

    public void send(String topic, String msg) {
        KeyedMessage<Integer, String> data = new KeyedMessage<>(topic, msg);
        producer.send(data);
        producer.close();
    }
}
