package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base class of our events. We want that each event has at least the field "type" in order to recognize the method to
 * use to process it.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    protected String type;
}