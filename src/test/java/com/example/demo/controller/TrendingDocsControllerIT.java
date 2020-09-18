package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.events.DocumentsEventsListener;
import com.example.demo.model.OpenDocumentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class TrendingDocsControllerIT {

    @Autowired
    TrendingDocsController controllerUnderTest;

    @Autowired
    DocumentsEventsListener eventsListener;

    List<OpenDocumentEvent> events;

    List<UUID> openedDocumentsIds = Stream.generate(UUID::randomUUID).limit(30).collect(Collectors.toList());

    @BeforeEach
    void setUp() throws Exception {
        prepareDocumentsEvents();
    }

    private static LocalDateTime getRandomDateTime(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        LocalDate randomDay = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay));

        final Random random = new Random();
        LocalTime randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60), 1 + random.nextInt(59));

        return LocalDateTime.of(randomDay, randomTime);
    }

    class DocumentEventSupplier implements Supplier<OpenDocumentEvent> {

        LocalDate start = LocalDate.of(2020, 9, 01);
        LocalDate end = LocalDate.of(2020, 9, 15);

        public OpenDocumentEvent get() {
            UUID eventId = UUID.randomUUID();
            UUID documentId = openedDocumentsIds.get(new Random().nextInt(openedDocumentsIds.size()));
            return new OpenDocumentEvent(eventId, documentId, getRandomDateTime(start, end));
        }
    }

    @Test
    void testGetLastTrendingDocuments() {

        String strDate1 = "2020-09-01";
        String strDate2 = "2020-09-08";

        List<Entry<String, Long>> trendingDocuments = controllerUnderTest.getLastTrendingDocuments(strDate1, strDate2);
        System.out.println(" ---> Trending Documents size: " + trendingDocuments.size());

        assertTrue(!trendingDocuments.isEmpty());
        assertTrue(trendingDocuments.size() <= TrendingDocsController.RESULT_MAX_SIZE);
    }

    private void prepareDocumentsEvents() {

        // TODO this should be consumed from Kafka test container
        Stream.generate(new DocumentEventSupplier()::get)
                .limit(100)
                .sorted((e1, e2) -> e1.getCreated().compareTo(e2.getCreated())).forEach(event -> {
                    JSONObject documentEventJson = new JSONObject();
                    try {
                        documentEventJson.put("eventId", event.getEventId());
                        documentEventJson.put("documentId", event.getDocumentId());
                        documentEventJson.put("timeStampCreated", event.getCreated());
                        eventsListener.listen(documentEventJson.toString());
                    } catch (JSONException | JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
    }
}
