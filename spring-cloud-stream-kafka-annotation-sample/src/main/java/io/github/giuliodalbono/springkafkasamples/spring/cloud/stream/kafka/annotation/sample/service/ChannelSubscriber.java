package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.model.Event;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.util.ApplicationContextProvider;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.util.ObjectMapperFactory;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.AutoConfiguredKafkaListener;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.evaluation.MethodEvaluator;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.binding.BindingTargetFactory;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class which serves to build Spring Cloud Stream channels and subscribe consumers to them.
 */
@Component
@RequiredArgsConstructor
public final class ChannelSubscriber {
    private final BindingService bindingService;
    private final BindingTargetFactory channelFactory;
    private final ConfigurableListableBeanFactory beanFactory;
    private final BindingServiceProperties bindingServiceProperties;

    private static final String DOT = ".";
    private static final String SPRING_CLOUD_INPUT_NAME_SUFFIX = "-in-0";

    /**
     * Given methods annotated with {@link AutoConfiguredKafkaListener}, create a channel for each unique pair of
     * "topicName" and "consumerGroup".
     *
     * @param scannedMethods Methods annotated with {@link AutoConfiguredKafkaListener}.
     */
    public void subscribeAll(List<Method> scannedMethods) {
        Map<Pair<String, String>, List<Method>> map = scannedMethods.stream()
                .collect(Collectors.groupingBy(m -> Pair.of(
                        m.getAnnotation(AutoConfiguredKafkaListener.class).topicName(),    // Left
                        m.getAnnotation(AutoConfiguredKafkaListener.class).consumerGroup() // Right
                )));

        map.forEach((topicConsumerPair, methods) -> {
            String topicName = topicConsumerPair.getLeft();
            String consumerGroup = topicConsumerPair.getRight();
            String inputName = topicName.concat(DOT).concat(consumerGroup).concat(SPRING_CLOUD_INPUT_NAME_SUFFIX);
            this.createConsumerChannel(topicName, inputName, consumerGroup, methods);
        });
    }

    /**
     * Creates a consumer channel. The handler will have the internal logic to dispatch messages to the correct
     * processor which matches with the event type.
     *
     * @param topic         Kafka topic name.
     * @param inputName     Spring Cloud Stream channel name.
     * @param consumerGroup Kafka consumer group
     * @param methods       methods which have the same unique pair of "topicName" and "consumerGroup".
     */
    public void createConsumerChannel(String topic, String inputName, String consumerGroup, List<Method> methods) {
        if (bindingServiceProperties.getBindings().containsKey(inputName)) {
            return;
        }
        BindingProperties bindingProperties = this.createBindingProperties(topic, consumerGroup);

        this.updateBindingServiceProperties(inputName, bindingProperties, bindingServiceProperties);

        SubscribableChannel channel = this.createChannel(inputName);

        channel.subscribe(new EventHandler(methods));
    }

    /**
     * Creates {@link BindingProperties} with the default consumer properties.
     *
     * @param topic Spring Cloud Stream channel destination.
     * @param group Spring Cloud Stream group.
     * @return The bindingProperties with given configuration.
     */
    private BindingProperties createBindingProperties(String topic, String group) {
        ConsumerProperties consumerProperties = new ConsumerProperties();
        BindingProperties bindingProperties = new BindingProperties();
        bindingProperties.setConsumer(consumerProperties);
        bindingProperties.setDestination(topic);
        bindingProperties.setGroup(group);
        return bindingProperties;
    }

    /**
     * Adds to the bindings of {@link BindingServiceProperties}, the given new binding.
     *
     * @param inputName                Spring Cloud Stream inputName.
     * @param bindingProperties        Properties of the binding to add.
     * @param bindingServiceProperties {@link BindingServiceProperties} bean.
     */
    private void updateBindingServiceProperties(String inputName, BindingProperties bindingProperties,
                                                BindingServiceProperties bindingServiceProperties) {
        Map<String, BindingProperties> updatedBindings = new HashMap<>(bindingServiceProperties.getBindings());
        updatedBindings.put(inputName, bindingProperties);
        bindingServiceProperties.setBindings(updatedBindings);
    }

    /**
     * Creates a {@link SubscribableChannel} and registers it to the {@link ConfigurableListableBeanFactory}, binding
     * the given inputName as consumer.
     *
     * @param inputName Spring Cloud Stream inputName.
     * @return the registered channel.
     */
    private SubscribableChannel createChannel(String inputName) {
        SubscribableChannel channel = (SubscribableChannel) channelFactory.createInput(inputName);
        beanFactory.registerSingleton(inputName, channel);
        channel = (SubscribableChannel) beanFactory.initializeBean(channel, inputName);
        bindingService.bindConsumer(channel, inputName);
        return channel;
    }
}

/**
 * Message handler for processors annotated with {@link AutoConfiguredKafkaListener}.
 */
@Slf4j
@RequiredArgsConstructor
final class EventHandler implements MessageHandler {
    private final List<Method> methods;

    /**
     * Method which handles incoming events.
     *
     * @param message the message to be handled.
     * @throws MessagingException If Something went wrong.
     * @see MethodEvaluator
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleMessage(@Nonnull Message<?> message) throws MessagingException {
        // We can do whatever type of preprocessing.
        this.preProcessMessage(message);

        // Search for a method which can process this message.
        Method method = methods.stream()
                .filter(meth -> new MethodEvaluator().test(meth, message))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No method which accepts this event type has been found"));

        // Get the type of method's parameter. Casting is unchecked because MethodEvaluator already checked it.
        Class<? extends Event> paramType = (Class<? extends Event>) method.getParameterTypes()[0];
        Event event = this.mapPayloadToParamType(message, paramType);

        // Invoking the processor
        this.processMessage(method, event);

        // We can do whatever type of postprocessing.
        this.postProcessMessage(message);
    }

    /**
     * Processing the message invoking the method.
     *
     * @param method which has to process the event.
     * @param param  actual parameter to pass to the method (event).
     * @throws RuntimeException If something went wrong during the invocation of the processor.
     */
    private void processMessage(Method method, Object param) throws RuntimeException {
        Class<?> loadedClass = method.getDeclaringClass();
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        Object bean = applicationContext.getBean(loadedClass);
        try {
            method.invoke(bean, param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Preprocess actions to perform.
     *
     * @param message Message to handle.
     */
    private void preProcessMessage(Message<?> message) {
        log.info(String.format("Preprocessing this message:\n%s", message));
    }

    /**
     * Postprocess actions to perform.
     *
     * @param message Message to handle.
     */
    private void postProcessMessage(Message<?> message) {
        log.info(String.format("Postprocessing this message:\n%s", message));
    }

    /**
     * Maps message's payload to the type of method's parameter.
     *
     * @param message   Message being handled.
     * @param paramType Formal parameter's type.
     * @return Mapped event.
     * @throws RuntimeException If Json errors occur.
     */
    private Event mapPayloadToParamType(Message<?> message, Class<? extends Event> paramType) throws RuntimeException {
        try {
            return ObjectMapperFactory.jsonMapper().readValue(String.valueOf(message.getPayload()), paramType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}