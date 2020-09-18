package com.example.demo.controller;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.DocumentDailyAggregation;
import com.example.demo.repository.DailyRecordsRepository;

@RestController
@RequestMapping("/documents")
public class TrendingDocsController {
    
    /**
     * Max size of result list of trending documents.
     */
    protected static final int RESULT_MAX_SIZE = 10;
    
    @Autowired
    DailyRecordsRepository repo;
    
    /**
     * 
     * @param date1 - String representation of date in form yyyy-MM-dd
     * @param date2 - String representation of date in form yyyy-MM-dd
     * 
     * @return Aggregated trending documents,  how many times which documents were opened in the period. 
     * Returns only @see RESULT_MAX_SIZE records of the most trending documents.
     */
    @GetMapping("/trending")
    public List<Entry<String, Long>> getLastTrendingDocuments(@RequestParam String date1, @RequestParam String date2) {
        
        LocalDate dateFrom = LocalDate.parse(date1);
        LocalDate dateTo = LocalDate.parse(date2);
        
        // dateFrom must by less than dateTo
        if (dateFrom.compareTo(dateTo) > 0) {
            LocalDate temp = dateFrom;
            dateFrom = dateTo;
            dateTo = temp;
        }
        
        System.out.println("*----> dateFrom: " + dateFrom); //TODO use logger
        System.out.println("*----> dateTo: " + dateTo); //TODO use logger
        
        // fetch aggregated daily record between the dates 
        List<DocumentDailyAggregation> dailyRecords = repo.findByDateGreaterThanEqualAndDateLessThanEqual(dateFrom, dateTo);
        
        System.out.println("*----> Historical Aggregated Daily Records: " + dailyRecords.size()); //TODO use logger
        
        Map<UUID, LongSummaryStatistics> sumarizedRecords = dailyRecords.stream()
            .collect(
                    Collectors.groupingBy(
                            DocumentDailyAggregation::getDocId, Collectors.summarizingLong(DocumentDailyAggregation::getCount)
                    )
            );
        
        
        // find trending documents according the count of opening
        //TODO add the condition of increasing tendency
        final List<Entry<String, Long>> trendingDocuments = sumarizedRecords.entrySet().stream()
                .map(i -> {
                    Map.Entry<String, Long> entry =
                            new AbstractMap.SimpleEntry<String, Long>(i.getKey().toString(), i.getValue().getSum());
                    return entry;
                })
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(RESULT_MAX_SIZE)
                .collect(Collectors.toList());
        
        return trendingDocuments;
    }
}
