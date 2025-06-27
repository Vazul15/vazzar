package com.vazzarmoviedb.radarrbackend.model.dto.response;

import com.vazzarmoviedb.radarrbackend.model.dto.FieldsNameValueDTO;

import java.util.List;

public record IndexerRequestDTO(
        String name,
        String implementation,
        String configContract,
        boolean supportsRss,
        boolean supportsSearch,
        boolean supportsRecent,
        int priority,
        String protocol,
        boolean enableRss,
        boolean enableAutomaticSearch,
        boolean enableInteractiveSearch,
        List<FieldsNameValueDTO> fields
) {}




