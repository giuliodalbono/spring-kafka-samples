package io.github.giuliodalbono.springkafkasamples.spring.cloud.stream.kafka.annotation.sample.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * Utility class for Json mapping.
 */
public final class ObjectMapperFactory {
    private ObjectMapperFactory() {
        throw new IllegalStateException("Utility class cannot be instanced!");
    }

    public static JsonMapper jsonMapper() {
        return configureJsonMapper();
    }

    private static JsonMapper configureJsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .addModule(new ParameterNamesModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }
}