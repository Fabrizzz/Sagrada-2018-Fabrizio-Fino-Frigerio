package it.polimi.se2018.model;

import java.util.ArrayList;

public class Board {
    private String name;
    private int tockens;
    private ArrayList<String> restrictions;

    public Board(String name, int tockens, ArrayList<String> restrictions){
        this.name = name;
        this.tockens = tockens;
        this.restrictions = restrictions;
    }

    public String getName() {
        return name;
    }

    public int getTockens() {
        return tockens;
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
