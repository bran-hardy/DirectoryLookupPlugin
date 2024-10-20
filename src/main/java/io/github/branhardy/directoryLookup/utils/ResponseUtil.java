package io.github.branhardy.directoryLookup.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.branhardy.directoryLookup.models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ResponseUtil {
    public static List<Shop> getShops(String response) {
        List<Shop> shops = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray results = jsonObject.getAsJsonArray("results");

        for (int i = 0; i < results.size(); i ++) {
            JsonObject page = results.get(i).getAsJsonObject();
            JsonObject properties = page.getAsJsonObject("properties");

            String title = extractTitle(properties.getAsJsonObject("Shop Name"));
            String coordinates = extractRichText(properties.getAsJsonObject("Coords (X, Z)"));
            List<String> spawn = extractMultiSelect(properties.getAsJsonObject("Spawn"));

            shops.add(new Shop(
                    title,
                    coordinates,
                    spawn.isEmpty() ? "" : spawn.getFirst()
            ));
        }

        return shops;
    }

    // Helper method to extract the "title" field
    private static String extractTitle(JsonObject titleProperty) {
        JsonArray titleArray = titleProperty.getAsJsonArray("title");
        return !titleArray.isEmpty() ? titleArray.get(0).getAsJsonObject()
                .getAsJsonObject("text").get("content").getAsString() : "";
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
                .getAsJsonObject("text").get("content").getAsString() : "";
    }
}
