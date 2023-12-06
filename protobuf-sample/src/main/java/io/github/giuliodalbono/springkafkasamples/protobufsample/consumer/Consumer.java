package io.github.giuliodalbono.springkafkasamples.protobufsample.consumer;

import io.github.giuliodalbono.springkafkasamples.protobufsample.MessageProto;
import io.github.giuliodalbono.springkafkasamples.protobufsample.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer class containing {@link KafkaListener} which consume deserializing Kafka record value using Protobuf.
 *
 * @see io.github.giuliodalbono.springkafkasamples.protobufsample.producer.Producer Producer
 * @see KafkaListener
 */
@Slf4j
@Service
public class Consumer {
    /**
     * KafkaListener which consumes using Protobuf deserializer as configured in application.yaml.
     * Record value type is {@link io.github.giuliodalbono.springkafkasamples.protobufsample.MessageProto.Message}
     * because in application.yaml is configured:<br>
     * "{@code spring.kafka.consumer.properties.specific.protobuf.value.type}" so the record is automatically
     * deserialized to that type.
     *
     * @param record ConsumerRecord consumed.
     *
     * @see ConsumerRecord
     */
    @KafkaListener(topics = {Constants.TOPIC}, groupId = Constants.GROUP_ID)
    public void consume(ConsumerRecord<String, MessageProto.Message> record) {
        log.info("Message id: " + record.value().getId());
        log.info("Message description: " + record.value().getDescription());
    }
}