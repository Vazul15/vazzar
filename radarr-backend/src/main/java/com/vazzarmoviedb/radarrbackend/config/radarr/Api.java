package com.vazzarmoviedb.radarrbackend.config.radarr;

public class Api {
    private String key;
    private String host;
    private int port;


    public String getKey() {
        return key;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}