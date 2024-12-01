package io.github.branhardy.directoryLookup.utils;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

public class FilterUtil {

    // This will add additional filters for diamond tools and armor (i.e. if someone searches for diamond_axe,
    // diamond_tools will be added to the filter)
    //
    // Additionally, it will filter out specific sherds to a simple "sherds" name since our notion doesn't contain
    // specific sherd names
    public static List<String> setupNotionFilter(String initialFilter) {
        List<String> filterList = new ArrayList<>();

 /*
        Map<String, Object> filters = DirectoryLookup.instance
                .getConfig()
                .getConfigurationSection("filters")
                .getValues(false);

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
        }

 */
        filterList.add(reformatFilter(initialFilter));

        Map<String, List<String>> configFilters = getFilterList();

        for (Map.Entry<String, List<String>> entry : configFilters.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            if (values.contains(initialFilter)) {
                filterList.add(reformatFilter(key));
            }
        }

        DirectoryLookup.logger.info("Filter has been created: " + filterList);

        return filterList;
    }

    private static List<String> getItemsWithSuffix(String matchingSuffix) {
        return Arrays.stream(Material.values())
                .map(material -> material.name().toLowerCase(Locale.ROOT))
                .filter(name -> name.endsWith(matchingSuffix))
                .collect(Collectors.toList());
    }

    // Reformat the input filter so that it can properly query the notion database
    private static String reformatFilter(String initialFilter) {
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

    private static Map<String, List<String>> getFilterList() {
        Map<String, List<String>> filters = new HashMap<>();

        List<?> rawFilters = DirectoryLookup.instance.getConfig().getList("filters");
        List<List<String>> filterList = new ArrayList<>();

        // Convert YML array to the Map, where the key is the filters name and its values the items to filtered
        if (rawFilters != null) {
            for (Object rawFilter : rawFilters) {
                if (rawFilter instanceof List<?> rawInnerList) {
                    List<String> filter = new ArrayList<>();

                    String filterName = "";
                    for (Object item : rawInnerList) {

                        if (item instanceof String) {
                            if (item == rawInnerList.getFirst()) {
                                filterName = (String) item;
                            } else {
                                filter.add((String) item);
                            }
                        }
                    }

                    filters.put(filterName, filter);
                }
            }
        }

        // Check for "_" in the times to filter and adjust the list accordingly
        for (Map.Entry<String, List<String>> entry : filters.entrySet()){
            String key = entry.getKey();
            List<String> values = entry.getValue();

            // If a filter group can be filtered using the Material values, it will start with _
            if (values.getFirst().startsWith("_")){
                values = getItemsWithSuffix(values.getFirst().toLowerCase(Locale.ROOT));

                filters.put(key, values);
            }
        }

        return filters;
    }
}
