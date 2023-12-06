package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.scan;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.AutoConfiguredKafkaListener;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.publisher.AnnotationScanPublisher;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.subscriber.AnnotationScanListener;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Record used for exchanging messages between publisher:
 * {@link AnnotationScanPublisher AnnotationScanPublisher}
 * and listener:
 * {@link AnnotationScanListener AnnotationScanListener}.
 *
 * @param scannedMethods Contains all methods in the context which are annotated with {@link AutoConfiguredKafkaListener}.
 * @see AutoConfiguredKafkaListenerScanner
 */
public record AutoConfiguredKafkaListenerScan(List<Method> scannedMethods) {
}