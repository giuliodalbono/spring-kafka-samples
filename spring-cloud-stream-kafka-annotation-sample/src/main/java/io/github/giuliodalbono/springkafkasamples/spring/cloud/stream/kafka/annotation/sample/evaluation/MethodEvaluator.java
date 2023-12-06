package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.evaluation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.model.Event;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.util.ObjectMapperFactory;
import org.springframework.messaging.Message;

import java.lang.reflect.Method;
import java.util.function.BiPredicate;

/**
 * BiPredicate which tests if the given method is able to process the given message.
 */
public final class MethodEvaluator implements BiPredicate<Method, Message<?>> {
    /**
     * First it verifies if the message has at least the field containing its type.
     * After that it verifies if the method has the parameter of the same type of the message.
     *
     * @param method  method to evaluate.
     * @param message message which we want to process.
     * @return True if the parameter is of the same type of the message. False otherwise.
     * @throws RuntimeException if the message has not the field containing its type.
     */
    @Override
    public boolean test(Method method, Message<?> message) {
        Event event;
        try {
            event = ObjectMapperFactory.jsonMapper().readValue(String.valueOf(message.getPayload()), Event.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // We don't care about out of bound because we have enforced that annotated parameter must have exactly 1
        // parameter and to be of type Event.
        return method.getParameterTypes()[0].getSimpleName().equals(event.getType());
    }
}