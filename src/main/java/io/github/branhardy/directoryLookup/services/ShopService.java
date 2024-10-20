package io.github.branhardy.directoryLookup.services;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.utils.FilterUtil;
import io.github.branhardy.directoryLookup.utils.ResponseParser;

import java.util.List;

public class ShopService {
    private final NotionService notionService;
    private final String databaseId;

    public ShopService(NotionService notionService, String databaseId) {
        this.notionService = notionService;
        this.databaseId = databaseId;
    }

    public List<Shop> getShops(String targetItem) {
        String response = notionService.queryDatabase(databaseId, FilterUtil.setupNotionFilter(targetItem));

        return ResponseParser.getShops(response);
    }
}
