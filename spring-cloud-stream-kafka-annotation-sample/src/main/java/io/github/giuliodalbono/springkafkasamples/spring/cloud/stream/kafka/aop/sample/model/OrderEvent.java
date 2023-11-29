package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Event representing an order.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class OrderEvent extends Event {
    private Long id;
    private List<ItemEvent> itemEvents;

    public OrderEvent(String type, Long id, List<ItemEvent> itemEvents) {
        super(type);
        this.id = id;
        this.itemEvents = new ArrayList<>(itemEvents);
    }
}