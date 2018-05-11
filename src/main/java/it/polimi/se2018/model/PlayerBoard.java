package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.BoardName;

public class PlayerBoard {
    private BoardName boardName;
    private Cell[] cells = new Cell[20];

    public PlayerBoard() {
        AbstractRestrictionFactory factory = AbstractRestrictionFactory.getFactory();
        for (int i = 0; i < cells.length; i++) { //da completare

        }
    }

    private Cell get(int row, int column) {

        return cells[column + 5 * row];

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


        return ris;
    }

    public boolean verifyInitialPositionRestriction(int row, int column) {

        return (row == 0 || row == 3 || column == 0 || column == 4);

    }

}
