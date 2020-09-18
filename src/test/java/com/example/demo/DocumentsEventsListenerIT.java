//package com.example.demo;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.assertj.core.groups.Tuple;
//import org.junit.jupiter.api.Test;
//import org.rnorth.ducttape.unreliables.Unreliables;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
//
//@Testcontainers
//class DocumentsEventsListenerIT {
//
//    @Value(value = "${kafka.topic.documents.name}")
//    private static String documentsTopic;
//
//    @Autowired
//    DocumentsEventsListener listener;
//
//    @Container
//    static KafkaContainer kafka = new KafkaContainer();
//
//    @Test
//    public void test_container_should_be_running() {
//        assertTrue(kafka.isRunning());
//    }
//    
//    @Test
//    public void testListener() throws Exception {
//        assertTrue(kafka.isRunning());
//        
//        testKafkaFunctionality(kafka.getBootstrapServers());
//
//        send1000DocumentsMessages(kafka.getBootstrapServers(), documentsTopic);
//    }
//
//    private void send1000DocumentsMessages(String bootstrapServers, String documentsTopic2) {
//        // TODO Auto-generated method stub
//
//    }
//
//    protected void testKafkaFunctionality(String bootstrapServers) throws Exception {
//        try (KafkaProducer<String, String> producer = new KafkaProducer<>(
//                ImmutableMap.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
//                        ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()),
//                new StringSerializer(), new StringSerializer());
//
//                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
//                        ImmutableMap.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
//                                ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
//                                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"),
//                        new StringDeserializer(), new StringDeserializer());) {
//            String topicName = "messages";
//            consumer.subscribe(Arrays.asList(topicName));
//
//            producer.send(new ProducerRecord<>(topicName, "testcontainers", "rulezzz")).get();
//
//            Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
//                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//
//                if (records.isEmpty()) {
//                    return false;
//                }
//
//                assertThat(records).hasSize(1)
//                        .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
//                        .containsExactly((Tuple[]) tuple(topicName, "testcontainers", "rulezzz"));
//
//                return true;
//            });
//
//            consumer.unsubscribe();
//        }
//    }
//
//    private Object tuple(String topicName, String string, String string2) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//}
