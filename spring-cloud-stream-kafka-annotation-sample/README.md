# Spring Cloud Stream Kafka Annotation Sample

This sample demonstrates how to make a method be the consumer of a spring cloud channel, simply annotating it with
an annotation and avoiding the ton of configuration in application properties for each channel.

## Steps to run application

- Install Docker and make sure it's running.

- Build the application with `mvn compile`.

- Run this application.

- Test the application and see the behavior calling these APIs:
  - http://localhost:8080/SpringCloudAnnotationSample/sendItemEventTo/test-topic
  - http://localhost:8080/SpringCloudAnnotationSample/sendItemEventTo/test-topic2
  - http://localhost:8080/SpringCloudAnnotationSample/sendOrderEventTo/test-topic
  - http://localhost:8080/SpringCloudAnnotationSample/sendOrderEventTo/test-topic2