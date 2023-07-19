# Protobuf Sample

This sample demonstrate how to use Protocol Buffer as serializer/deserializer in a Spring Boot
application using Spring for Apache Kafka.

It is made up of 3 fundamental components:

- Producer - a service which basically produces Kafka messages using `KafkaTemplate`.

- Consumer - a service which is a basic `KafkaListener`.

- SenderController - a controller which make the Producer send a Kafka message.

## How to use Protobuf

### Dependencies and Plugins

To use Protobuf these dependencies are needed:

- [protobuf-java](https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java) - documentation [here](https://protobuf.dev/getting-started/javatutorial/).

- [kafka-protobuf-serializer](https://mvnrepository.com/artifact/io.confluent/kafka-protobuf-serializer) - documentation [here](https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-protobuf.html).

And this plugin is needed:

- [protoc-jar-maven-plugin](https://mvnrepository.com/artifact/com.github.os72/protoc-jar-maven-plugin) - Needed to generate Java classes from .proto files. Documentation [here](https://github.com/os72/protoc-jar-maven-plugin/blob/master/README.md).

### Schemas

In order to use Protobuf, we first need to write our own proto schemas and in this sample
it is located under [src/main/resources/proto](src/main/resources/proto).  
Thanks to `protoc-jar-maven-plugin` schemas will be translated to Java classes, so
we will be able to use them as inferred type of respectively ConsumerRecord value type
and KafkaTemplate value type for Consumer and Producer.  
Note that the type we want to use is the inner class, in this sample it's `MessageProto.Message`

### Configuration

In order to use Protobuf with Spring for Apache Kafka we need to configure:

- `spring.kafka.producer.value-serializer=io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer`  
to configure the producer serializer to use Protobuf

- `spring.kafka.consumer.value-deserializer=io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer`  
to configure the consumer deserializer to use Protobuf

- `spring.kafka.producer.properties.schema.registry.url=http://localhost:8085`  
to configure the producer to use this schema registry to save proto schemas

- `spring.kafka.consumer.properties.schema.registry.url=http://localhost:8085`  
to configure the consumer to use this schema registry to fetch proto schemas

- `spring.kafka.consumer.properties.specific.protobuf.value.type=io.github.giuliodalbono.springkafkasamples.protobufsample.MessageProto$Message`  
to configure the consumer to automatically deserialize Kafka records' values into objects of type `MessageProto.Message`

## Steps to run application

- Install Docker and make sure it's running.

- Build the application with `mvn compile`.

- Run this application.

- Make an API call to http://localhost:8080/ProtobufSample/sendMessage, you
should see the produced and consumed message in console log.