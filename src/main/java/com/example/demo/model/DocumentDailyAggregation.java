package com.example.demo.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.example.demo.repository.compositePK.AggregationId;

@Entity
@IdClass(AggregationId.class)
public class DocumentDailyAggregation {
    
    //date and docId as PrimaryKey
    @Id
    private LocalDate date;
    
    @Id
    private UUID docId;
    
    private Long count;
    
    
    //it is here only because of hibernate
    @SuppressWarnings("unused")
    private DocumentDailyAggregation() {
    }

    public DocumentDailyAggregation(LocalDate date, UUID docId, Long count) {
        this.date = date;
        this.docId = docId;
        this.count = count;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public UUID getDocId() {
        return this.docId;
    }
    
    public Long getCount() {
        return this.count;
    }
}
