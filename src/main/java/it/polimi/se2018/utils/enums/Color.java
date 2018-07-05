package it.polimi.se2018.utils.enums;

/**
 * Enumerazione dei colori dei dadi
 * @author Giampietro
 */
public enum Color {

    RED("Rosso"),
    YELLOW("Giallo"),
    GREEN("Verde"),
    BLUE("Blu"),
    PURPLE("Viola");

    String str;

    Color(String str) {
        this.str = str;
    }

    public String getColorString() {
        return str;
    }
}
