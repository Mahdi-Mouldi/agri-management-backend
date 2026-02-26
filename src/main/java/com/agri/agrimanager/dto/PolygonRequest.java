package com.agri.agrimanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolygonRequest {
    private String name;

    @JsonProperty("geo_json")
    private JsonNode geo_json;  // ← change Object → JsonNode
}