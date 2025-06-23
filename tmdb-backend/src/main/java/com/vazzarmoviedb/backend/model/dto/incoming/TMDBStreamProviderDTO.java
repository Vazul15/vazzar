package com.vazzarmoviedb.backend.model.dto.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TMDBStreamProviderDTO(@JsonProperty("display_priority") Integer displayPriority,
                                    @JsonProperty("provider_name") String providerName,
                                    @JsonProperty("provider_id") Integer providerId,
                                    @JsonProperty("logo_path") String logoPath) {
    public String getLogoPath() {
        return logoPath == null ? null : "https://image.tmdb.org/t/p/original/" + logoPath;
    }
}
