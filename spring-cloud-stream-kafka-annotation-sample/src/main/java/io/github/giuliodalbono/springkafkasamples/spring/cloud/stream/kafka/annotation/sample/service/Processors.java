package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.service;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.model.ItemEvent;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.model.OrderEvent;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.AutoConfiguredKafkaListener;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Example of class containing processors.
 * Having these processors, will create 3 different channels as follows:
 * <ul>
 *     <li>
 *         destination: test-topic<br>
 *         inputName: test-topic.test-in-0<br>
 *         group: test<br>
 *         processors:<br>
 *           - processor1<br>
 *           - processor2<br>
 *     </li>
 *     <li>
 *         destination: test-topic2<br>
 *         inputName: test-topic2.test-in-0<br>
 *         group: test<br>
 *         processors:<br>
 *           - processor3<br>
 *     </li>
 *     <li>
 *         destination: test-topic2<br>
 *         inputName: test-topic2.test2-in-0<br>
 *         group: test2<br>
 *         processors:<br>
 *           - processor4<br>
 *     </li>
 * </ul>
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public final class Processors {
    @AutoConfiguredKafkaListener(topicName = Constants.TEST_TOPIC, consumerGroup = Constants.TEST_CONSUMER_GROUP)
    public void processor1(ItemEvent itemEvent) {
        log.info(String.format("This is processor1 processing:\n%s", itemEvent));
    }

    @AutoConfiguredKafkaListener(topicName = Constants.TEST_TOPIC, consumerGroup = Constants.TEST_CONSUMER_GROUP)
    public void processor2(OrderEvent orderEvent) {
        log.info(String.format("This is processor2 processing:\n%s", orderEvent));
    }

    @AutoConfiguredKafkaListener(topicName = Constants.TEST_TOPIC_2, consumerGroup = Constants.TEST_CONSUMER_GROUP)
    public void processor3(ItemEvent itemEvent) {
        log.info(String.format("This is processor3 processing:\n%s", itemEvent));
    }

    @AutoConfiguredKafkaListener(topicName = Constants.TEST_TOPIC_2, consumerGroup = Constants.TEST_CONSUMER_GROUP_2)
    public void processor4(OrderEvent orderEvent) {
        log.info(String.format("This is processor4 processing:\n%s", orderEvent));
    }
}