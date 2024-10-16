package io.github.branhardy.directoryLookup.services;

import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.utils.ResponseParser;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ShopService {
    private final NotionService notionService;
    private final String databaseId;
    private final Logger logger;

    public ShopService(NotionService notionService, String databaseId, Logger logger) {
        this.notionService = notionService;
        this.databaseId = databaseId;
        this.logger = logger;
    }

    public CompletableFuture<List<Shop>> getShops() {
        return notionService.queryDatabase(databaseId)
                .thenApply(ResponseParser::getShops)
                .exceptionally(ex -> { logger.warning(ex.getMessage()); return null; });
    }
}
