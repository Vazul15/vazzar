package com.vazzarmoviedb.radarrbackend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Service
public class IndexerConfService {

    @Value("${radarr.api.key}")
    private String radarrApiKey;

    @Value("${radarr.api.host}")
    private String radarrApiHost;

    @Value("${radarr.api.port}")
    private int radarrApiPort;

    private String getRadarrIndexerBaseUrl(){
        return "http://"+radarrApiHost+":"+radarrApiPort+"/api/v3/indexer";
    }

    public void getIndexerr() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", radarrApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

}