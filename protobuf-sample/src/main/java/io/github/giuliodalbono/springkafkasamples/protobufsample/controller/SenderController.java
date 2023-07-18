package io.github.giuliodalbono.springkafkasamples.protobufsample.controller;

import io.github.giuliodalbono.springkafkasamples.protobufsample.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for demo purpose which cause the producer send an event.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ProtobufSample")
public class SenderController {
    private final Producer producer;

    /**
     * Produces a demo event.
     * @return String to return to client.
     */
    @GetMapping("/sendMessage")
    public ResponseEntity<String> sendMessage() {
        String message = producer.produce();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}