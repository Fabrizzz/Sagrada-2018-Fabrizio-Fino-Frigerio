package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private String name;
    private int tokens;
    private ArrayList<String> restrictions;

    public Board(String name, int tokens, ArrayList<String> restrictions) {
        this.name = name;
        this.tokens = tokens;
        this.restrictions = restrictions;
    }

    public String getName() {
        return name;
    }

    public int getTokens() {
        return tokens;
    }

    public String getRestriction(int row,int column){
        return restrictions.get(column + row*5);
    }

    public String[] getRestrictions() {
        String[] stringArray = new String[restrictions.size()];
        stringArray = restrictions.toArray(stringArray);
        return stringArray;
    }

}
