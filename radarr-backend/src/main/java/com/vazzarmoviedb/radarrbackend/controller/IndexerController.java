package com.vazzarmoviedb.radarrbackend.controller;

import com.vazzarmoviedb.radarrbackend.model.dto.request.indexer.IndexerNameJackettApiKeyTorzNabUrlDTO;
import com.vazzarmoviedb.radarrbackend.service.IndexerConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/indexer")
public class IndexerController {

    private final IndexerConfService indexerConfService;

    @Autowired
    public IndexerController(IndexerConfService indexerConfService) {
        this.indexerConfService = indexerConfService;
    }

    @PostMapping("/add-indexer")
    public Mono<Void> addIndexer(@RequestBody IndexerNameJackettApiKeyTorzNabUrlDTO newIndexer) {
        return indexerConfService.addIndexer(newIndexer);
    }

}
