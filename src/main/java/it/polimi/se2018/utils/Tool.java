package it.polimi.se2018.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Tool {
    MOSSASTANDARD("Mossa Standard"),
    PINZASGROSSATRICE("Pinza Sgrossatrice"),
    PENNELLOPEREGLOMISE("Pennello Per Eglomise"),
    ALESATOREPERLAMINADIRAME("Alesatore Per Lamina Di Rame"),
    LATHEKIN("Lathekin"),
    TAGLIERINACIRCOLARE("Taglierina Circolare"),
    PENNELLOPERPASTASALDA("Pennello Per Pasta Salda"),
    MARTELLETTO("Martelletto"),
    TENAGLIAAROTELLE("Tenaglia A Rotelle"),
    RIGAINSUGHERO("Riga In Sughero"),
    TAMPONEDIAMANTATO("Tampone Di Amantato"),
    DILUENTEPERPASTASALDA("Diluente Per Pasta Salda"),
    TAGLIERINAMANUALE("Taglierina Manuale");

    private String toolName;

    Tool(String str) {
        toolName = str;
    }

    public static Tool[] getRandTools(int n) {
        List<Tool> tools = Arrays.asList(Tool.values());
        Collections.shuffle(tools);
        return tools.subList(0, n).toArray(new Tool[0]);
    }

    @Override
    public String toString() {
        return toolName;
    }

}
