package com.vazzarmoviedb.radarrbackend.service;
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


//    Indexer DTO thats what I will do when I will know what wil lbe exactly
//    public Mono<List<IndexerDto>> getIndexers() {
//        return radarrClient.get()
//                .uri("indexer")
//                .retrieve()
//                .bodyToFlux(IndexerDto.class)
//                .collectList();
//    }
}
