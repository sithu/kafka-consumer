package consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import({ MailConfiguration.class })
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
        //KafkaConsumer kafkaConsumer = new KafkaConsumer("54.149.84.25:2181", "test-group", "test-topic");
        //kafkaConsumer.consume();
    }
}