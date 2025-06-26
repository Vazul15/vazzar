package com.vazzarmoviedb.radarrbackend.model.dto.request;

import com.vazzarmoviedb.radarrbackend.model.dto.FieldsNameValueDTO;

import java.util.List;

public record TorznabRequestDTO(
        String name,
        String implementation,
        String configContract,
        boolean supportsRss,
        boolean supportsSearch,
        boolean supportsRecent,
        int priority,
        String protocol,
        List<FieldsNameValueDTO> fields
) {}




