package it.polimi.se2018.utils;

public enum BoardName {
    KALEIDOSCOPICDREAM("Kaleidoscopi Dream", "VIRTUS", 4, "YELLOW", "BLUE", "", "", "ONE", "GREEN", "", "FIVE", "", "FOUR", "THREE", "", "RED", "", "GREEN", "TWO", "", "", "BLUE", "YELLOW"),
    VIRTUS("Virtus", "KALEIDOSCOPICDREAM", 5, "FOUR", "", "TWO", "FIVE", "GREEN", "", "", "SIX", "GREEN", "TWO", "", "THREE", "GREEN", "FOUR", "", "FIVE", "GREEN", "ONE", "", "");
    /*VIALUX("Via Lux"),
    AURORAEMAGNIFICUS("Aurorare Magnificus")*/; //Da completare


    String name;
    String coppia;
    String[] restrictions;
    int tokens;


    BoardName(String str, String coppia, int tokens, String... restrictions) {
        name = str;
        this.coppia = coppia;
        this.tokens = tokens;
        if (restrictions.length != 20)
            throw new IllegalArgumentException();
        this.restrictions = restrictions;
    }

    public String getName() {
        return name;
    }

    public BoardName getCoppia() {
        return BoardName.valueOf(coppia);
    }

    public String[] getRestrictions() {
        return restrictions;
    }

    public String getRestriction(int row, int column) {
        return getRestrictions()[column + 5 * row];
    }

    public int getTokens() {
        return tokens;
    }

}
