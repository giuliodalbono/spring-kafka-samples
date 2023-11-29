package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which indicates that a method is eligible for being a Kafka processor, at the given topic and consumer
 * group.<br>
 * Annotated methods must have exactly one argument and be void.<br>
 * A new Spring Cloud Stream channel will be created for each unique pair of topicName and consumerGroup.<br>
 * Distinction for multiple processors on same channel will be performed based on the method parameter type.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoConfiguredKafkaListener {
    String topicName();

    String consumerGroup();
}