package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.DocumentDailyAggregation;

@Repository
public interface DailyRecordsRepository extends CrudRepository<DocumentDailyAggregation, Long> {

    List<DocumentDailyAggregation> findByDateGreaterThanEqualAndDateLessThanEqual(LocalDate date1, LocalDate date2);
}
