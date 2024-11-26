package com.yeoro.domain.webClient.model.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlacePhotoService {

    private final PlaceService placeService;

    public PlacePhotoService(PlaceService placeService) {
        this.placeService = placeService;
    }

    public Mono<String> saveAndGetPhotoUrl(String photoName) {
        if (photoName == null || photoName.isEmpty()) {
            return Mono.empty();
        }

        int maxHeightPx = 400;
        int maxWidthPx = 400;

        String url = String.format("/%s/media?maxHeightPx=%d&maxWidthPx=%d&key=%s", photoName, maxHeightPx, maxWidthPx, placeService.getApiKey());

        return placeService.getJsonResponse(url, null, null)
                .map(responseBody -> placeService.extractPhotoUri(responseBody.toString()));
    }
}
