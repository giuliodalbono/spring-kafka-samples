package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event representing an Item.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class ItemEvent extends Event {
    private Long id;
    private String description;
    private Integer price;

    public ItemEvent(String type, Long id, String description, Integer price) {
        super(type);
        this.id = id;
        this.description = description;
        this.price = price;
    }
}