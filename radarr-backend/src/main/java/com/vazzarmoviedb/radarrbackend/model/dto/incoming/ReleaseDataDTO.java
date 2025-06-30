package com.vazzarmoviedb.radarrbackend.model.dto.incoming;

import java.util.List;

public record ReleaseDataDTO(
        Integer id,
        String guid,
        ReleaseQualityWrapperDTO quality,
        Integer indexerId,
        String indexer,
        List<ReleaseLanguagesDTO> languages,
        Boolean rejected,
        Integer seeders
        //Boolean downloadAllowed
        ) {
}