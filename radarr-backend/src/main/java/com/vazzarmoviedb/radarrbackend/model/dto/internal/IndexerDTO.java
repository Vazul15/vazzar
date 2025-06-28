package com.vazzarmoviedb.radarrbackend.model.dto.internal;

import java.util.List;

public record IndexerDTO(
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
        List<IndexerFieldsNameValueDTO> fields
) {
}


