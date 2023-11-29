package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.configuration.annotation.scan;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.configuration.annotation.AutoConfiguredKafkaListener;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Record used for exchanging messages between publisher:
 * {@link io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.publisher.AnnotationScanPublisher AnnotationScanPublisher}
 * and listener:
 * {@link io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.subscriber.AnnotationScanListener AnnotationScanListener}.
 *
 * @param scannedMethods Contains all methods in the context which are annotated with {@link AutoConfiguredKafkaListener}.
 * @see AutoConfiguredKafkaListenerScanner
 */
public record AutoConfiguredKafkaListenerScan(List<Method> scannedMethods) {
}