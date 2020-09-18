package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OpenDocumentEvent;

@Repository
public interface OpenDocumentEventRepository extends CrudRepository<OpenDocumentEvent, UUID> {

    // TODO must support only create and readAll from default CRUD operations    

    Collection<OpenDocumentEvent> findByTimeStampCreatedLessThan(LocalDateTime todayMidnight);
}
