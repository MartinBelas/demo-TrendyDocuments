package com.example.demo.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OpenDocumentEvent {
    
    @Id
    @Column(length = 36)
    private UUID eventId;
    
    @Column(length = 36)
    private UUID documentId;
    
    @Column
    private LocalDateTime timeStampCreated;
    
    //it is here only because of hibernate
    @SuppressWarnings("unused")
    private OpenDocumentEvent() {
    }

    public OpenDocumentEvent(UUID eventId, UUID documentId) {
        this.eventId = eventId;
        this.documentId = documentId;
        this.timeStampCreated = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }
    
    public OpenDocumentEvent(UUID eventId, UUID documentId, LocalDateTime timeStampCreated) {
        this.eventId = eventId;
        this.documentId = documentId;
        this.timeStampCreated = timeStampCreated;
    }

    public UUID getEventId() {
        return this.eventId;
    }
    
    public UUID getDocumentId() {
        return this.documentId;
    }
    
    public LocalDateTime getCreated() {
        return this.timeStampCreated;
    }
}
