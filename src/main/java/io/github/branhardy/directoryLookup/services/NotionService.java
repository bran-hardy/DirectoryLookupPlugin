package io.github.branhardy.directoryLookup.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class NotionService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiUrl;
    private final String apiKey;
    private final String apiVersion;

    public NotionService(String apiUrl, String apiKey, String apiVersion, Logger logger) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
    }

    public CompletableFuture<String> queryDatabase(String database) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + database + "/query"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Notion-Version", apiVersion)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }
}
