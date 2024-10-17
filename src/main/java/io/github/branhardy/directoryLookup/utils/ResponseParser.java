package io.github.branhardy.directoryLookup.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.branhardy.directoryLookup.models.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseParser {
    public static List<Shop> getShops(String response) {
        List<Shop> shops = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray results = jsonObject.getAsJsonArray("results");

        for (int i = 0; i < results.size(); i ++) {
            JsonObject page = results.get(i).getAsJsonObject();
            JsonObject properties = page.getAsJsonObject("properties");

            List<String> inventory = extractMultiSelect(properties.getAsJsonObject("Inventory"))
                    .stream()
                    .map(item -> item.toLowerCase().trim().replace(" ", "_"))
                    .collect(Collectors.toList());

            shops.add(new Shop(
                    extractTitle(properties.getAsJsonObject("Shop Name")),
                    inventory,
                    extractRichText(properties.getAsJsonObject("Coords (X, Z)")),
                    extractMultiSelect(properties.getAsJsonObject("Spawn")).getFirst()
            ));
        }

        return shops;
    }

    // Helper method to extract the "title" field
    private static String extractTitle(JsonObject titleProperty) {
        JsonArray titleArray = titleProperty.getAsJsonArray("title");
        return !titleArray.isEmpty() ? titleArray.get(0).getAsJsonObject()
                .getAsJsonObject("text").get("content").getAsString() : "Unnamed Shop";
    }

    // Helper method to extract the "multi_select" field
    private static List<String> extractMultiSelect(JsonObject itemsProperty) {
        JsonArray multiSelectArray = itemsProperty.getAsJsonArray("multi_select");
        List<String> items = new ArrayList<>();

        for (int i = 0; i < multiSelectArray.size(); i++) {
            items.add(multiSelectArray.get(i).getAsJsonObject().get("name").getAsString());
        }

        return items;
    }

    // Helper method to extract the "rich_text" field
    private static String extractRichText(JsonObject richTextProperty) {
        JsonArray richTextArray = richTextProperty.getAsJsonArray("rich_text");
        return !richTextArray.isEmpty() ? richTextArray.get(0).getAsJsonObject()
                .getAsJsonObject("text").get("content").getAsString() : "No Info Found";
    }
}
