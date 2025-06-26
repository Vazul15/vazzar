package com.vazzarmoviedb.radarrbackend.service;

import com.vazzarmoviedb.radarrbackend.model.dto.FieldsNameValueDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.IndexerNameJackettApiKeyTorzNabUrlDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.TorznabRequestDTO;
import com.vazzarmoviedb.radarrbackend.model.enums.MovieCategory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class IndexerConfService {

    private final WebClient radarrClient;

    public IndexerConfService(WebClient radarrClient) {
        this.radarrClient = radarrClient;
    }

//    public Mono<List<IndexerDto>> getIndexers() {
//        return radarrClient.get()
//                .uri("indexer")
//                .retrieve()
//                .bodyToFlux(IndexerDto.class)
//                .collectList();
//    }

    public Mono<Void> addIndexer(IndexerNameJackettApiKeyTorzNabUrlDTO indexerRequestDTO) {
        FieldsNameValueDTO baseUrl = new FieldsNameValueDTO("baseUrl", indexerRequestDTO.torznabUrl());
        FieldsNameValueDTO apiKey = new FieldsNameValueDTO("apiKey", indexerRequestDTO.jackettApiKey());
        FieldsNameValueDTO categories = new FieldsNameValueDTO("categories", MovieCategory.GENERAL.getCode());

        TorznabRequestDTO newIndexer = new TorznabRequestDTO(
                indexerRequestDTO.name(),
                "Torznab",
                "TorznabSettings",
                true,
                true,
                true,
                1,
                "torrent",
                List.of(baseUrl, apiKey, categories)
        );

        return radarrClient.post()
                .uri("indexer")
                .bodyValue(newIndexer)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
