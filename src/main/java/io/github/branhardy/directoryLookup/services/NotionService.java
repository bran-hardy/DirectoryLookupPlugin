package io.github.branhardy.directoryLookup.services;

import io.github.branhardy.directoryLookup.DirectoryLookup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class NotionService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiUrl;
    private final String apiKey;
    private final String apiVersion;

    public NotionService(String apiUrl, String apiKey, String apiVersion) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
    }

    public String queryDatabase(String database, List<String> filter) {
        String jsonBody = createJsonFilter(filter);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + database + "/query"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Notion-Version", apiVersion)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = null;

        try {
            DirectoryLookup.logger.info("Requesting SHOP database json from Notion Api");
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            DirectoryLookup.logger.severe("Failed to get Notion database info");
        }

        return response != null ? response.body() : "";
    }

    private String createJsonFilter(List<String> filters) {
        StringBuilder filter = new StringBuilder();

        filter.append("\"or\":[");
        for (String item : filters) {
            filter.append("{ \"property\": \"Inventory\", \"multi_select\": { \"contains\": \"")
                    .append(item)
                    .append("\" }}");

            if (!filters.getLast().equals(item)) {
                filter.append(",");
            }
        }
        filter.append("]");

        return "{ \"filter\": { " + filter + "}}";
    }
}
