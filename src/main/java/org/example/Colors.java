package org.example;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    private static final Map<String, String> colorMap = new HashMap<>();

    static  {
        colorMap.put("red", "#FF0000");
        colorMap.put("blue", "#0000FF");
        colorMap.put("green", "#00FF00");
        colorMap.put("yellow", "#FFFF00");
        colorMap.put("cyan", "#00FFFF");
        colorMap.put("magenta", "#FF00FF");
        colorMap.put("orange", "#FFA500");
        colorMap.put("purple", "#800080");
    }

    public static String getHex(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), "#000000"); // Default to black if color not found
    }
}
