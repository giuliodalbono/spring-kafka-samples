package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.scan;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.configuration.annotation.AutoConfiguredKafkaListener;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.evaluation.MethodValidator;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.publisher.AnnotationScanPublisher;
import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.utils.ApplicationContextProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.springframework.util.ReflectionUtils.MethodFilter;

/**
 * Configuration class which serves to initialize (and keep updated) the methods annotated with
 * {@link AutoConfiguredKafkaListener}.<br>
 * Each time the application context refreshes (so also at startup), it publishes the methods which it has scanned.
 */
@Configuration
@RequiredArgsConstructor
public class AutoConfiguredKafkaListenerScanner implements ApplicationListener<ContextRefreshedEvent> {
    private final AnnotationScanPublisher annotationScanPublisher;

    /**
     * When a ContextRefreshedEvent is received, it performs the scan and validation of the methods, publishing the
     * result via {@link AnnotationScanPublisher}.
     *
     * @param event {@link ContextRefreshedEvent} event to respond to.
     * @throws RuntimeException If there is at least one annotated method which has not passed the validation.
     */
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) throws RuntimeException {
        List<Method> scannedMethods = this.scanMethods();
        AutoConfiguredKafkaListenerScan scan = new AutoConfiguredKafkaListenerScan(scannedMethods);
        this.annotationScanPublisher.publishEvent(scan);
    }

    /**
     * Looks at all beans in the context, searching for methods annotated with {@link AutoConfiguredKafkaListener}.
     * All scanned methods are added into a local list.<br>
     * After this process of scanning, methods are validated.
     *
     * @return The scanned methods which are annotated with {@link AutoConfiguredKafkaListener}
     * @throws RuntimeException If there is at least one annotated method which has not passed the validation.
     * @see AutoConfiguredKafkaListenerScanner#validateScannedMethods(List)
     */
    private List<Method> scanMethods() throws RuntimeException {
        List<Method> scannedMethods = new ArrayList<>();

        List<? extends Class<?>> candidateClasses =
                Arrays.stream(ApplicationContextProvider.getApplicationContext().getBeanDefinitionNames())
                        .map(beanName -> ApplicationContextProvider.getApplicationContext().getBean(beanName).getClass())
                        .filter(beanClz -> AnnotationUtils.isCandidateClass(beanClz, AutoConfiguredKafkaListener.class))
                        .toList();

        candidateClasses.forEach(beanClz -> new PopulateWithAnnotatedMethods().accept(beanClz, scannedMethods));

        this.validateScannedMethods(scannedMethods);
        return scannedMethods;
    }

    /**
     * Validates the methods which have been scanned.
     *
     * @param scannedMethods Methods which have been scanned.
     * @throws RuntimeException If there is at least one method which does not respect the constraints.
     * @see MethodValidator
     */
    private void validateScannedMethods(List<Method> scannedMethods) throws RuntimeException {
        List<Method> invalidMethods = scannedMethods.stream()
                .filter(meth -> new MethodValidator().test(meth)).toList();
        if (!CollectionUtils.isEmpty(invalidMethods)) {
            throw new RuntimeException(String.format("""
                            Method annotated with %s must have only one argument!
                            Here all methods which do not respect this constraint:
                            %s""",
                    AutoConfiguredKafkaListener.class,
                    Arrays.toString(invalidMethods.toArray())
            ));
        }
    }
}

/**
 * BiConsumer which selects methods annotated with {@link AutoConfiguredKafkaListener} (if any) and store them in the
 * given list of methods.
 */
class PopulateWithAnnotatedMethods implements BiConsumer<Class<?>, List<Method>> {
    /**
     * Checks if is there any method with {@link AutoConfiguredKafkaListener} annotation, and whenever it's present,
     * adds it to the given list of methods.
     *
     * @param beanClz        Bean class to check.
     * @param scannedMethods list of already scanned methods to update (if possible).
     */
    @Override
    public void accept(Class<?> beanClz, List<Method> scannedMethods) {
        Set<Method> methods = MethodIntrospector.selectMethods(
                beanClz,
                (MethodFilter) meth -> meth.isAnnotationPresent(AutoConfiguredKafkaListener.class)
        );
        if (!methods.isEmpty()) {
            scannedMethods.addAll(methods);
        }
    }
}