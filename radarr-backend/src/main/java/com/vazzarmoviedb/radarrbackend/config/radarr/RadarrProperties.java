package com.vazzarmoviedb.radarrbackend.config.radarr;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "radarr")
public class RadarrProperties {
    private Api api;
    private String rootFolder;

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Api getApi() {
        return api;
    }

    public String getHost() {
        return api != null ? api.getHost() : null;
    }

    public int getPort() {
        return api != null ? api.getPort() : -1;
    }

    public String getApiKey() {
        return api != null ? api.getKey() : null;
    }
}


