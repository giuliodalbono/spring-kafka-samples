package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.configuration.annotation;

import io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.model.Event;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Set;

import static javax.tools.Diagnostic.Kind;

/**
 * Annotation processor which verifies that each method annotated with {@link AutoConfiguredKafkaListener} has the
 * correct signature (void return type and exactly one argument of type {@link Event} or its subclass).
 */
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.aop.sample.configuration.annotation.AutoConfiguredKafkaListener")
public final class KafkaListenerAnnProcessor extends AbstractProcessor {
    /**
     * Annotation processor for {@link AutoConfiguredKafkaListener}.
     *
     * @param annotations the annotation interfaces requested to be processed.
     * @param roundEnv    environment for information about the current and prior round.
     * @return True if each of the following is true:
     * <ul>
     *     <li>
     *         Annotation is applied only to methods.
     *     </li>
     *     <li>
     *         Return type of every method is void.
     *     </li>
     *     <li>
     *         Every method accept exactly one parameter of type {@link Event} or its subclass.
     *     </li>
     * </ul>
     * False otherwise.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement expectedParameterType = processingEnv.getElementUtils().getTypeElement(Event.class.getCanonicalName());
        String expectedReturnType = void.class.getCanonicalName();

        for (Element element : roundEnv.getElementsAnnotatedWith(AutoConfiguredKafkaListener.class)) {
            if (!(element instanceof ExecutableElement executableElement)) {
                processingEnv.getMessager().printMessage(
                        Kind.ERROR,
                        String.format(
                                "Annotation \"%s\" should be only applied to methods.",
                                AutoConfiguredKafkaListener.class.getCanonicalName()
                        )
                );
                continue;
            }
            if (!String.valueOf(executableElement.getReturnType()).equals(expectedReturnType)) {
                processingEnv.getMessager().printMessage(
                        Kind.ERROR,
                        String.format(
                                "Method \"%s\" should return %s but instead it returns %s",
                                executableElement,
                                expectedReturnType,
                                executableElement.getReturnType()
                        )
                );
            }
            List<? extends VariableElement> parameters = executableElement.getParameters();
            if (parameters.size() != 1 || !processingEnv.getTypeUtils().isAssignable(parameters.get(0).asType(), expectedParameterType.asType())) {
                processingEnv.getMessager().printMessage(
                        Kind.ERROR,
                        String.format(
                                "Method \"%s\" should have single %s argument but instead it has %s",
                                executableElement,
                                expectedParameterType,
                                parameters.get(0).asType()
                        )
                );
            }
        }
        return true;
    }
}