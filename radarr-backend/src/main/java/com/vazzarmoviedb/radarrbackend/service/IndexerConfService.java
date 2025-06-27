package com.vazzarmoviedb.radarrbackend.service;

import com.vazzarmoviedb.radarrbackend.model.dto.FieldsNameValueDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.indexer.IndexerNameJackettApiKeyTorzNabUrlDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.response.IndexerRequestDTO;
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


    public Mono<Void> addIndexer(IndexerNameJackettApiKeyTorzNabUrlDTO indexerRequestDTO) {

        IndexerRequestDTO newIndexer = new IndexerRequestDTO(
                indexerRequestDTO.name(),
                "Torznab",
                "TorznabSettings",
                true,
                true,
                true,
                1,
                "torrent",
                true,
                true,
                true,
                List.of(
                        new FieldsNameValueDTO("baseUrl", indexerRequestDTO.torznabUrl()),
                        new FieldsNameValueDTO("apiPath", "/api"),
                        new FieldsNameValueDTO("apiKey", indexerRequestDTO.jackettApiKey()),
                        new FieldsNameValueDTO("categories", List.of(2000))
                )
        );

        return radarrClient.post()
                .uri("indexer")
                .bodyValue(newIndexer)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
