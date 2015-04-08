package consumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by saung on 4/7/15.
 */
public class KafkaConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private final EmailService emailService;
    private final ConsumerConnector consumer;
    private final String topic;

    public KafkaConsumer(String zookeeper, String groupId, String topic, EmailService emailService) {
        this.emailService = emailService;
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void consume() {
        Map<String, Integer> topicCount = new HashMap<>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                String msg = new String(it.next().message());
                LOG.info("##### Message from Topic: {} #########", msg);
                List<String> result = parseMsg(msg);
                if (result.size() > 2) {
                    emailService.send(result.get(0), "CMPE273-Assignment-2-" + result.get(1) + "- Poll Result", result.get(2));
                } else {
                    LOG.error("Invalid message format:{}", msg);
                }
            }
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    private List<String> parseMsg(String msg) {
        List<String> result = new ArrayList();

        if (StringUtils.isEmpty(msg)) {
            return result;
        }

        for (String each : msg.split(":")) {
            result.add(each);
        }

        return result;
    }

}