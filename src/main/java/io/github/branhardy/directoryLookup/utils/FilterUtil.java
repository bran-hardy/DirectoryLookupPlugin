package io.github.branhardy.directoryLookup.utils;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FilterUtil {

    public static List<String> pottery_sherd_names;
    public static List<String> armor_trim_template_names;

    public static List<String> diamond_tools, diamond_armor;
    public static List<String> horse_armor;

    public static List<String> flowers, dyes;

    public static void initialize() {
        pottery_sherd_names = getItemsWithSuffix("_SHERD");
        armor_trim_template_names = getItemsWithSuffix("_TRIM_SMITHING_TEMPLATE");

        diamond_tools = Arrays.asList(
                "diamond_pickaxe",
                "diamond_axe",
                "diamond_shovel",
                "diamond_hoe",
                "diamond_sword"
        );

        diamond_armor = Arrays.asList(
                "diamond_helmet",
                "diamond_chestplate",
                "diamond_leggings",
                "diamond_boots"
        );

        horse_armor = Arrays.asList(
                "leather_horse_armor",
                "iron_horse_armor",
                "golden_horse_armor",
                "diamond_horse_armor"
        );

        flowers = Arrays.asList(
                "allium",
                "azure_bluet",
                "blue_orchid",
                "cornflower",
                "dandelion",
                "lily_of_the_valley",
                "oxeye_daisy",
                "poppy",
                "torch_flower",
                "orange_tulip",
                "pink_tulip",
                "red_tulip",
                "white_tulip",
                "wither_rose",
                "lilac",
                "peony",
                "pitcher_plant",
                "rose_bush",
                "sunflower"
        );

        dyes = getItemsContaining("_DYE");

        DirectoryLookup.logger.info("Filters have been initialized");
    }

    public static List<String> getItemsWithSuffix(String matchingSuffix) {
        return Arrays.stream(Material.values())
                .map(Material::name)
                .filter(name -> name.endsWith(matchingSuffix))
                .collect(Collectors.toList());
    }

    public static List<String> getItemsContaining(String matchingName) {
        return Arrays.stream(Material.values())
                .map(Material::name)
                .filter(name -> name.contains(matchingName))
                .collect(Collectors.toList());
    }

    // This will add additional filters for diamond tools and armor (i.e. if someone searches for diamond_axe,
    // diamond_tools will be added to the filter)
    //
    // Additionally, it will filter out specific sherds to a simple "sherds" name since our notion doesn't contain
    // specific sherd names
    public static List<String> setupNotionFilter(String initialFilter) {
        List<String> filterList = new ArrayList<>();

        filterList.add(reformatFilter(initialFilter));

        if (pottery_sherd_names.contains(initialFilter)) filterList.add("Sherds");
        if (armor_trim_template_names.contains(initialFilter)) filterList.add(" Armor Trims");
        if (diamond_tools.contains(initialFilter)) filterList.add("Diamond Tools");
        if (diamond_armor.contains(initialFilter)) filterList.add("Diamond Armor");
        if (horse_armor.contains(initialFilter)) filterList.add("Horse Armor");
        if (flowers.contains((initialFilter))) filterList.add("Flowers");
        if (dyes.contains((initialFilter))) filterList.add("Dye");

        DirectoryLookup.logger.info("Filter has been created: " + filterList);

        return filterList;
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
}
