package it.polimi.se2018.view;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;

import java.util.HashMap;
import java.util.Map;

public class GUIUtils {
    private static Map<Color,String> colorMap = new HashMap<>();

    public static String colorToString(Color color){
        colorMap.clear();
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

        return colorMap.get(color);
    }
}
