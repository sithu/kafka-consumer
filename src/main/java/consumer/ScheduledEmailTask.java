package consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by saung on 4/7/15.
 */
@Component
public class ScheduledEmailTask {

    @Autowired
    private EmailService emailService;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.url}")
    private String kafkaBrokerUrl;

    @Scheduled(fixedRate = 5000)
    public void consumeMessages() {
        System.out.println("The time is now " + new Date());
        KafkaConsumer kafkaConsumer = new KafkaConsumer(kafkaBrokerUrl, "test-group", topic, emailService);
        kafkaConsumer.consume();
    }
}
