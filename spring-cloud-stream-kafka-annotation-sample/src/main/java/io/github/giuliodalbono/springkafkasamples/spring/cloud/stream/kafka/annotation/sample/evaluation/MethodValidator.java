package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.evaluation;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.model.Event;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * Predicate which tests if a method declaration is valid
 */
public final class MethodValidator implements Predicate<Method> {
    /**
     * Tests if the given method has exactly one parameter and of type {@link Event} and the return type is void.
     *
     * @param method method to validate.
     * @return True if the given method has:
     * <ul>
     *     <li>
     *         Exactly one parameter.
     *     </li>
     *     <li>
     *         The parameter is of type {@link Event}.
     *     </li>
     *     <li>
     *         The return type of the method is void.
     *     </li>
     * </ul>
     * False otherwise.
     */
    @Override
    public boolean test(Method method) {
        // We force annotated method to have only 1 parameter and to be of Event type.
        return method.getParameterTypes().length == 1 &&
                method.getParameterTypes()[0].isAssignableFrom(Event.class) &&
                method.getReturnType().equals(Void.class);
    }
}
