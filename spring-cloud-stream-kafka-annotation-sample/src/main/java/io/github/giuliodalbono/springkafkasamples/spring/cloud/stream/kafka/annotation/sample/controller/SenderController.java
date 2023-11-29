package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.producer.Producer;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for demo purpose which cause the producer send an event.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/AopSample")
public class SenderController {
    private final Producer producer;

    /**
     * Produces a demo itemEvent to topic "test-topic".
     *
     * @return String to return to client.
     */
    @GetMapping("/sendItemEventTo/test-topic")
    public ResponseEntity<String> sendItemEventToTestTopic() throws JsonProcessingException {
        String message = producer.produceItemEvent(Constants.TEST_TOPIC);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Produces a demo itemEvent to topic "test-topic2".
     *
     * @return String to return to client.
     * @implNote This will cause an exception since the processors defined for this topic, belong to different consumer
     * groups, so no processor will be found for the "ItemEvent" type on the channel which belongs to "test2" consumer
     * group.
     */
    @GetMapping("/sendItemEventTo/test-topic2")
    public ResponseEntity<String> sendItemEventToTestTopic2() throws JsonProcessingException {
        String message = producer.produceItemEvent(Constants.TEST_TOPIC_2);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Produces a demo orderEvent to topic "test-topic".
     *
     * @return String to return to client.
     */
    @GetMapping("/sendOrderEventTo/test-topic")
    public ResponseEntity<String> sendOrderEventToTestTopic() throws JsonProcessingException {
        String message = producer.produceOrderEvent(Constants.TEST_TOPIC);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Produces a demo orderEvent to topic "test-topic2".
     *
     * @return String to return to client.
     * @implNote This will cause an exception since the processors defined for this topic, belong to different consumer
     * groups, so no processor will be found for the "OrderEvent" type on the channel which belongs to "test" consumer
     * group.
     */
    @GetMapping("/sendOrderEventTo/test-topic2")
    public ResponseEntity<String> sendOrderEventToTestTopic2() throws JsonProcessingException {
        String message = producer.produceOrderEvent(Constants.TEST_TOPIC_2);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}