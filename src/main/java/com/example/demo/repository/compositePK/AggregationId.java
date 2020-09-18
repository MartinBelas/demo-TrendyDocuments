package com.example.demo.repository.compositePK;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class AggregationId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private LocalDate date;
    private UUID docId;
 
    // must be here only because of hibernate
    @SuppressWarnings("unused")
    private AggregationId() {
    }
 
    public AggregationId(LocalDate date, UUID docId) {
        this.date = date;
        this.docId = docId;
    }
    
    public boolean equals(Object  o) {
        if (o == this)
            return true;
        if (!(o instanceof AggregationId))
            return false;
        AggregationId other = (AggregationId) o;
        boolean docIdEquals = (this.docId == null && other.docId == null)
          || (this.docId != null && this.docId.equals(other.docId));
        boolean dateEquals = (this.date == null && other.date == null)
          || (this.date != null && this.date.equals(other.date));
        return docIdEquals && dateEquals;
    }
    
    public final int hashCode() {
        int result = 17;
        if (docId != null) {
            result = 31 * result + docId.hashCode();
        }
        if (date != null) {
            result = 31 * result + date.hashCode();
        }
        return result;
        
    }
}