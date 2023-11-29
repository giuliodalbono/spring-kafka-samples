package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model.ItemEvent;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model.OrderEvent;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.utils.ObjectMapperFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Producer class which sends Kafka events (then consumed by the processors defined in
 * {@link io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.service.Processors Processors}).
 *
 * @see io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.service.Processors Processors
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Build a demo itemEvent to produce.
     *
     * @return A String to return to client.
     */
    public String produceItemEvent(String topic) throws JsonProcessingException {
        ItemEvent itemEvent = new ItemEvent("ItemEvent", 1L, "item description", 5);

        log.info("Sending this itemEvent: {}", itemEvent);
        String parsedItemEvent = ObjectMapperFactory.jsonMapper().writeValueAsString(itemEvent);

        // Sending message
        kafkaTemplate.send(topic, parsedItemEvent);

        return String.format("""
                The following message has been sent:<br>
                %s<br>
                Verify on console logs if it has been received""", parsedItemEvent);
    }

    /**
     * Build a demo orderEvent to produce.
     *
     * @return A String to return to client.
     */
    public String produceOrderEvent(String topic) throws JsonProcessingException {
        OrderEvent orderEvent = new OrderEvent(
                "OrderEvent",
                1L,
                List.of(
                        new ItemEvent(
                                "ItemEvent",
                                1L,
                                "item description",
                                5
                        )
                )
        );

        log.info("Sending this orderEvent: {}", orderEvent);
        String parsedOrderEvent = ObjectMapperFactory.jsonMapper().writeValueAsString(orderEvent);

        // Sending message
        kafkaTemplate.send(topic, parsedOrderEvent);

        return String.format("""
                The following message has been sent:<br>
                %s<br>
                Verify on console logs if it has been received""", parsedOrderEvent);
    }
}