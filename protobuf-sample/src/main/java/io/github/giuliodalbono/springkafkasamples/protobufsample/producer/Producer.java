package io.github.giuliodalbono.springkafkasamples.protobufsample.producer;

import io.github.giuliodalbono.springkafkasamples.protobufsample.MessageProto;
import io.github.giuliodalbono.springkafkasamples.protobufsample.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer class which sends Kafka events (then consumed by the
 * {@link io.github.giuliodalbono.springkafkasamples.protobufsample.consumer.Consumer Consumer}).
 *
 * @see io.github.giuliodalbono.springkafkasamples.protobufsample.consumer.Consumer Consumer
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, MessageProto.Message> kafkaTemplate;

    /**
     * Build a demo message to produce.
     * @return A String to return to client.
     */
    public String produce() {
        // Creating builder for MessageProto.Message instances.
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        // Building message
        builder.setId(1L).setDescription("test");
        MessageProto.Message message = builder.build();

        log.info("Sending this message: {}", message);
        // Sending message
        kafkaTemplate.send(Constants.TOPIC, message);

        return String.format("""
                The following message has been sent:<br>
                id: %s<br>
                description: %S<br><br>
                Verify on console logs if it has been received""", message.getId(), message.getDescription());
    }
}