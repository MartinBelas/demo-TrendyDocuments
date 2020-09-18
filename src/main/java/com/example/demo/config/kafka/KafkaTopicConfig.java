//package com.example.demo.config.kafka;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaAdmin;
//
//@Configuration
//public class KafkaTopicConfig {
//    
//    @Value(value = "${kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value(value = "${kafka.topic.documents.name}")
//    private static String documentsTopic;
// 
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        return new KafkaAdmin(configs);
//    }
//    
//    @Bean
//    public NewTopic topic1() {
//         return new NewTopic(documentsTopic, 1, (short) 1);
//    }
//}