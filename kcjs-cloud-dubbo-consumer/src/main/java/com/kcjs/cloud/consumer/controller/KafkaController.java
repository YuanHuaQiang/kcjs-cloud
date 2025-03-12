//package com.kcjs.cloud.consumer.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/kafka")
//public class KafkaController {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    public KafkaController(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    @PostMapping("/send")
//    public String sendMessage(@RequestBody String message) {
//        kafkaTemplate.send("test-topic", message);
//        return "Message sent to Kafka topic: " + message;
//    }
//
//    @PostMapping("/sendWithKey")
//    public String sendMessageWithKey(@RequestBody String message, @RequestParam String key) {
//        kafkaTemplate.send("test-topic", key, message);
//        return "Message sent to Kafka topic with key: " + key + ", message: " + message;
//    }
//
//
//    @PostMapping("/sendJson")
//    public String sendJsonMessage(@RequestBody MyMessage message) throws Exception {
//        String jsonMessage = objectMapper.writeValueAsString(message);
//        kafkaTemplate.send("test-topic", jsonMessage);
//        return "JSON Message sent to Kafka topic: " + jsonMessage;
//    }
//
//
//    @PostMapping("/sendWithCallback")
//    public String sendMessageWithCallback(@RequestBody String message) {
//        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("test-topic", message);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                System.err.println("Failed to send message: " + ex.getMessage());
//            } else {
//                System.out.println("Message sent successfully: " + result.toString());
//            }
//        });
//
//        return "Message sent to Kafka topic: " + message;
//    }
//
//
//    class MyMessage {
//        private String field1;
//        private String field2;
//
//        // Getters and Setters
//        public String getField1() {
//            return field1;
//        }
//
//        public void setField1(String field1) {
//            this.field1 = field1;
//        }
//
//        public String getField2() {
//            return field2;
//        }
//
//        public void setField2(String field2) {
//            this.field2 = field2;
//        }
//    }
//}
