package com.yeoro.domain.webClient.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PlaceService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.google.places.api-key}")
    private String apiKey;

    public PlaceService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://places.googleapis.com/v1").build();
        this.objectMapper = objectMapper;
    }

    public Mono<JsonNode> getJsonResponse(String url, String requestBody, String fieldMask) {
        return webClient.post()
                .uri(url)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", fieldMask)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    public String extractPhotoUri(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("photoUri").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getApiKey() {
        return apiKey;
    }
}
