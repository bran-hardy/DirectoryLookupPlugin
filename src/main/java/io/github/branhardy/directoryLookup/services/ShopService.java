package io.github.branhardy.directoryLookup.services;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
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
        String response = notionService.queryDatabase(databaseId, modifyTargetItemString(targetItem));

        DirectoryLookup.logger.info(response);

        return ResponseParser.getShops(response);
    }

    public String modifyTargetItemString(String targetItem) {
        String[] words = targetItem.split("_");
        StringBuilder outputString = new StringBuilder();

        for (int i = 0; i < words.length; i ++) {
            outputString.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));

            if (i != words.length - 1) outputString.append(" ");
        }

        return outputString.toString();
    }
}
