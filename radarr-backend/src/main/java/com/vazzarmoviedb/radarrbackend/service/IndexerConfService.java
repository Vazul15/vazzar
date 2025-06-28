package com.vazzarmoviedb.radarrbackend.service;

import com.vazzarmoviedb.radarrbackend.model.dto.internal.IndexerFieldsNameValueDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.request.IndexerNameJackettApiKeyTorzNabUrlDTO;
import com.vazzarmoviedb.radarrbackend.model.dto.internal.IndexerDTO;
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

        IndexerDTO newIndexer = new IndexerDTO(
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
                        new IndexerFieldsNameValueDTO("baseUrl", indexerRequestDTO.torznabUrl()),
                        new IndexerFieldsNameValueDTO("apiPath", "/api"),
                        new IndexerFieldsNameValueDTO("apiKey", indexerRequestDTO.jackettApiKey()),
                        new IndexerFieldsNameValueDTO("categories", List.of(2000))
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
