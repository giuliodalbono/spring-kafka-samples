package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.subscriber;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.AutoConfiguredKafkaListener;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.service.ChannelSubscriber;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.scan.AutoConfiguredKafkaListenerScan;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener of events of type {@link AutoConfiguredKafkaListenerScan}.
 */
@Component
@RequiredArgsConstructor
public final class AnnotationScanListener {
    private final ChannelSubscriber channelSubscriber;

    /**
     * Subscribes all given methods.
     *
     * @param scan methods annotated with
     *             {@link AutoConfiguredKafkaListener AutoConfiguredKafkaListener}.
     */
    @EventListener
    public void handleAnnotationScan(AutoConfiguredKafkaListenerScan scan) {
        channelSubscriber.subscribeAll(scan.scannedMethods());
    }
}