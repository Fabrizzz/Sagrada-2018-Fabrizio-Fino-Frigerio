package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.Restriction;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Board of the player
 * @author Giampietro
 */
public class PlayerBoard implements Serializable {
    private Board board;
    private Cell[] cells = new Cell[20];
    boolean isEmpty = true;

    /**
     * Costructor
     * @param board name of the board
     */
    public PlayerBoard(Board board) {
        AbstractRestrictionFactory factory = AbstractRestrictionFactory.getFactory();
        this.board = board;
        String[] restrictions = board.getRestrictions();
        Restriction restrizione;

        for (int i = 0; i < restrictions.length; i++) {
            String temp = restrictions[i];

            if (Arrays.stream(NumberEnum.values()).map(Enum::toString).anyMatch(numberEnum -> numberEnum.equals(temp)))
                restrizione = factory.createNumberRestriction(NumberEnum.valueOf(restrictions[i]));
            else if (Arrays.stream(Color.values()).map(Enum::toString).anyMatch(color -> color.equals(temp)))
                restrizione = factory.createColorRestriction(Color.valueOf(restrictions[i]));
            else if (restrictions[i].length() == 0)
                restrizione = factory.createNoRestriction();
            else
                System.err.println("Sintassi mappe JSON errata");
                throw new IllegalArgumentException();
            cells[i] = new Cell(restrizione);

        }
    }

    /**
     * Return the cell in the board in posizion row,column
     * @param row row of the cell
     * @param column column of the cell
     * @return cell in position row,column
     */
    private Cell get(int row, int column) {

        return cells[column + 5 * row];

    }

    /**
     * Return if the board contains at least a die
     * @return true if is empty, false otherwise
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Return the die in the cell in position row,column
     * @param row row of the cell
     * @param column column of the cell
     * @return die in the cell
     * @throws NoDieException if the cell is empty
     */
    public Die getDie(int row, int column) throws NoDieException {
        return get(row, column).getDie();
    }

    /**
     * Check if a cell contains a die
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the cell contains a die, false otherwise
     */
    public boolean containsDie(int row, int column) {
        return get(row, column).isUsed();
    }

    /**
     * Remove the die in the cell in position row,colunm
     * @param row row of the cell
     * @param column column of the cell
     * @throws NoDieException if the cell is empty
     */
    public void removeDie(int row, int column) throws NoDieException {
        get(row, column).removeDie();
    }

    /**
     * Insert a die in the cell in position row,column
     * @param die die 
     * @param row row of the cell
     * @param column column of the cell
     * @throws AlredySetDie if a die is already in the cell
     */
    public void setDie(Die die, int row, int column) throws AlredySetDie {
        get(row, column).setDie(die);
        isEmpty = false;
    }

    /**
     * Check if the die respect the color restriction of the cell
     * @param die die 
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the die respect the restriction, false otherwise
     */
    public boolean verifyColorRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isColorRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    /**
     * Check if the die respect the number restriction of the cell
     * @param die die 
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the die respect the restriction, false otherwise
     */
    public boolean verifyNumberRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isNumberRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    /**
     * Check if the position rules are respected for the cell in position row,column
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the restriction is respected, false otherwise
     */
    public boolean verifyPositionRestriction(int row, int column) {
        boolean ris = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0 || j != 0))
                    try {
                        if (get(row + i, column + j).isUsed())
                            ris = true;
                    } catch (IndexOutOfBoundsException e) {

                    }
            }
        }
        return ris;

    }

    /**
     * Check if the proximity position restriction can be respected by the die in the cell row,column
     * @param die die to be used in the check
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the restriction is respected, false otherwise
     */
    public boolean verifyNearCellsRestriction(Die die, int row, int column) {
        boolean ris = true;

        try {
            if (row > 0 && (get(row - 1, column).isUsed())) {
                if (get(row - 1, column).getDie().getColor().equals(die.getColor()) || get(row - 1, column).getDie().getNumber().equals(die.getNumber()))
                    ris = false;
            }

            if (row < 3 && (get(row + 1, column).isUsed())) {
                if (get(row + 1, column).getDie().getColor().equals(die.getColor()) || get(row + 1, column).getDie().getNumber().equals(die.getNumber()))
                    ris = false;
            }

            if (column > 0 && (get(row, column - 1).isUsed())) {
                if (get(row, column - 1).getDie().getColor().equals(die.getColor()) || get(row, column - 1).getDie().getNumber().equals(die.getNumber()))
                    ris = false;
            }

            if (column < 4 && (get(row, column + 1).isUsed())) {
                if (get(row, column + 1).getDie().getColor().equals(die.getColor()) || get(row, column + 1).getDie().getNumber().equals(die.getNumber()))
                    ris = false;
            }
        } catch (NoDieException e) {
            System.err.println(e);
        }


        return ris;
    }

    public boolean verifyInitialPositionRestriction(int row, int column) {

        return (row == 0 || row == 3 || column == 0 || column == 4);

    }

    public Restriction getRestriction(int row, int column) {
        return get(row, column).getRestriction();
    }

}
