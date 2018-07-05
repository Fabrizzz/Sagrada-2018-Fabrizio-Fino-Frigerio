package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Alessio
 */
public class Board implements Serializable {
    private final String name;
    private final int tokens;
    private final ArrayList<String> restrictions;

    /**
     * Constructor
     * @param name name of the board
     * @param tokens difficulty tokens
     * @param restrictions cell rules
     */
    public Board(String name, int tokens, ArrayList<String> restrictions) {
        this.name = name;
        this.tokens = tokens;
        this.restrictions = restrictions;
    }

    /**
     * Return the name of the board
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Return the difficulty tokens
     * @return the difficulty tokens
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Return the cell restriction at row,column
     * @param row row of the cell
     * @param column column of the cell
     * @return the restriction
     */
    public String getRestriction(int row,int column){
        return restrictions.get(column + row*5);
    }

    /**
     * Return all the restrictions
     * @return the array of the restrictions
     */
    public String[] getRestrictions() {
        String[] stringArray = new String[restrictions.size()];
        stringArray = restrictions.toArray(stringArray);
        return stringArray;
    }

}
