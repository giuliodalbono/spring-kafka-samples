package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.publisher;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.configuration.annotation.scan.AutoConfiguredKafkaListenerScan;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publisher of events of type {@link AutoConfiguredKafkaListenerScan}.
 */
@Component
@RequiredArgsConstructor
public final class AnnotationScanPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishEvent(AutoConfiguredKafkaListenerScan scan) {
        publisher.publishEvent(scan);
    }
}