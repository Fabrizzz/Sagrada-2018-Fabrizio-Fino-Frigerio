package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NoRestriction;
import it.polimi.se2018.utils.BoardName;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;

public class PlayerBoard implements Serializable {
    private BoardName boardName;
    private Cell[] cells = new Cell[20];

    public PlayerBoard() {
        AbstractRestrictionFactory factory = AbstractRestrictionFactory.getFactory();
        for (int i = 0; i < cells.length; i++) { //da completare
            cells[i] = new Cell(new NoRestriction());//inizializzazione temporanea per testing
        }
    }

    private Cell get(int row, int column) {

        return cells[column + 5 * row];

    }

    public Die getDie(int row, int column) throws NoDieException {
        return get(row, column).getDie();
    }

    public boolean containsDie(int row, int column) {
        return get(row, column).isUsed();
    }

    public void removeDie(int row, int column) throws NoDieException {
        get(row, column).removeDie();
    }

    public void setDie(Die die, int row, int column) throws AlredySetDie {
        get(row, column).setDie(die);
    }

    public boolean verifyColorRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isColorRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    public boolean verifyNumberRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isNumberRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    public boolean verifyPositionRestriction(int row, int column) {
        boolean ris = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (get(row + i, column + j).isUsed())
                        ris = true;
                } catch (IndexOutOfBoundsException e) {

                }
            }
        }
        return ris;

    }

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

}
