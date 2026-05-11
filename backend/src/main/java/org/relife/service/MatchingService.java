package org.relife.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.relife.dto.ResourceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MatchingService {

    @Value("${relife.matching.service.url:http://localhost:5000}")
    private String matchingServiceUrl;

    private final ResourceService resourceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchingService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public List<ResourceDTO> getMatches(String city, String category, Integer urgency) {
        List<ResourceDTO> all = resourceService.findAll();
        try {
            String url = matchingServiceUrl + "/match";
            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("resources", all.stream().map(r -> {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("resourceId", r.getResourceId());
                m.put("city", r.getCity());
                m.put("category", r.getCategory());
                m.put("urgency", r.getUrgency() != null ? r.getUrgency() : 0);
                return m;
            }).toList());
            body.put("user_city", city != null ? city : "");
            body.put("user_category", category != null ? category : "");
            body.put("urgency_weight", urgency != null ? urgency : 1);
            String json = objectMapper.writeValueAsString(body);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Integer[] ids = objectMapper.readValue(response.body(), Integer[].class);
                if (ids.length > 0) {
                    return Arrays.stream(ids)
                            .map(id -> all.stream().filter(r -> r.getResourceId().equals(id)).findFirst().orElse(null))
                            .filter(r -> r != null)
                            .collect(java.util.stream.Collectors.toList());
                }
            }
        } catch (Exception e) {
            // Fallback to local ordering when Python service unavailable
        }
        all.sort((a, b) -> {
            int cityMatch = (city != null && city.equals(a.getCity()) ? -1 : 0) - (city != null && city.equals(b.getCity()) ? -1 : 0);
            if (cityMatch != 0) return cityMatch;
            int urgencyCompare = (b.getUrgency() != null ? b.getUrgency() : 0) - (a.getUrgency() != null ? a.getUrgency() : 0);
            return urgencyCompare;
        });
        return all;
    }
}
