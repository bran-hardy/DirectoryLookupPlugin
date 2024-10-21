package io.github.branhardy.directoryLookup.utils;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

public class FilterUtil {

    private static Map<String, Object> filters;

    // This will add additional filters for diamond tools and armor (i.e. if someone searches for diamond_axe,
    // diamond_tools will be added to the filter)
    //
    // Additionally, it will filter out specific sherds to a simple "sherds" name since our notion doesn't contain
    // specific sherd names
    public static List<String> setupNotionFilter(String initialFilter) {
        List<String> filterList = new ArrayList<>();

        filters = DirectoryLookup.instance.getConfig().getConfigurationSection("filters").getValues(false);

        filterList.add(reformatFilter(initialFilter));

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String filterName = entry.getKey();
            List<String> items = castToStringList(entry.getValue());

            // If a filter group can be filtered using the Material values, it will start with _
            if (items.getFirst().startsWith("_")){
                items = getItemsWithSuffix(items.getFirst().toLowerCase(Locale.ROOT));
            }

            if (items.contains(initialFilter)) {
                filterList.add(reformatFilter(filterName));
            }

            DirectoryLookup.logger.info(reformatFilter(filterName) + ": " + items);
        }

        DirectoryLookup.logger.info("Filter has been created: " + filterList);

        return filterList;
    }

    public static List<String> getItemsWithSuffix(String matchingSuffix) {
        return Arrays.stream(Material.values())
                .map(material -> material.name().toLowerCase(Locale.ROOT))
                .filter(name -> name.endsWith(matchingSuffix))
                .collect(Collectors.toList());
    }

    // Reformat the input filter so that it can properly query the notion database
    public static String reformatFilter(String initialFilter) {
        String[] words = initialFilter.split("_");
        StringBuilder outputString = new StringBuilder();

        for (int i = 0; i < words.length; i ++) {
            outputString.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));

            if (i != words.length - 1) outputString.append(" ");
        }

        return outputString.toString();
    }

    private static List<String> castToStringList(Object obj) {
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .filter(element -> element instanceof String)
                    .map(element -> (String) element)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
