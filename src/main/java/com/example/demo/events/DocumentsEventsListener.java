package com.example.demo.events;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.model.DocumentDailyAggregation;
import com.example.demo.model.OpenDocumentEvent;
import com.example.demo.repository.DailyRecordsRepository;
import com.example.demo.repository.OpenDocumentEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class DocumentsEventsListener {
    
    private final Logger logger = LoggerFactory.getLogger(DocumentsEventsListener.class);
    
    @Autowired
    DailyRecordsRepository dailyRacordsrepo;
    
    @Autowired
    OpenDocumentEventRepository eventsRepo;

    private static final String DOCUMENTS_TOPICS = "DocumentOpened"; //TODO inject from properties file
    
    ObjectMapper mapper = new ObjectMapper();
    
    LocalDate dateOfLastEvent = null;

//    @KafkaListener(topics = DOCUMENTS_TOPICS, groupId = "group_id")
    public void listen(String eventJson) throws JsonMappingException, JsonProcessingException {
        
        logger.debug(String.format("//-> Listened Message: %s", eventJson));
        
        // parse consumed json String to Event Object
        JSONObject obj = new JSONObject(eventJson);
        UUID eId = UUID.fromString(obj.getString("eventId"));
        UUID dId = UUID.fromString(obj.getString("documentId"));
        String strTimeStamp = obj.getString("timeStampCreated").replace(" " , "T").replace("Z" , "") ;
        LocalDateTime timeStampCreated = LocalDateTime.parse(strTimeStamp, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        OpenDocumentEvent event = new OpenDocumentEvent(eId, dId, timeStampCreated); 
        
        // persist messages to 'events' table, 
        // when the date of new event is greater then the last previous one, 
        // aggregate the events records and persist them to the 'dailyRecords' table, 
        // clear the 'events' table and start again to persist events of the day to the 'events' table
        
        
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        
        Collection<OpenDocumentEvent> oldPersistedEvents = eventsRepo.findByTimeStampCreatedLessThan(todayMidnight);
        
        logger.debug(String.format("//-> oldPersistedEvents size: %d", oldPersistedEvents.size()));
        
        if (!oldPersistedEvents.isEmpty()) {
            aggregateOlderRecords(oldPersistedEvents);
        }
        
        eventsRepo.save(event);
        dateOfLastEvent = timeStampCreated.toLocalDate();
    }

    private void aggregateOlderRecords(Collection<OpenDocumentEvent> oldPersistedEvents) {
        
        Map<LocalDateTime, List<OpenDocumentEvent>> aggregatedByDate = StreamSupport.stream(oldPersistedEvents.spliterator(), false)
                .map(e -> {
                    LocalDate date = e.getCreated().toLocalDate();
                    LocalDateTime midnight = LocalDateTime.of(date, LocalTime.MIDNIGHT);
                    return new OpenDocumentEvent(e.getEventId(), e.getDocumentId(), midnight);   
                }) 
                .collect(Collectors.groupingBy(OpenDocumentEvent::getCreated));
        
        aggregatedByDate.forEach((date, events) -> {
            events.stream()
                .collect(Collectors.groupingBy(OpenDocumentEvent::getDocumentId, Collectors.counting()))
                .forEach((docId, count) -> {
                    persistAggregated(date, docId, count);
                });
        });
    }

    private void persistAggregated(LocalDateTime date, UUID docId, Long count) {
        dailyRacordsrepo.save(new DocumentDailyAggregation(date.toLocalDate(), docId, count));
    }
}
