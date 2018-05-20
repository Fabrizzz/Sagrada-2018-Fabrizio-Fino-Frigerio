package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.Restriction;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Plancia del giocatore
 * @author Giampietro
 */
public class PlayerBoard implements Serializable {
    private BoardName boardName;
    private Cell[] cells = new Cell[20];
    boolean isEmpty = true;

    /**
     * Costruttore
     */
    public PlayerBoard(BoardName boardName) {
        AbstractRestrictionFactory factory = AbstractRestrictionFactory.getFactory();
        this.boardName = boardName;
        String[] restrictions = boardName.getRestrictions();
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
                throw new IllegalArgumentException();
            cells[i] = new Cell(restrizione);

        }
    }

    /**
     * Restituisce la cella in riga row e colonna column
     * @param row riga della cella
     * @param column colonna della cella
     * @return cella in riga row e colonna column
     */
    private Cell get(int row, int column) {

        return cells[column + 5 * row];

    }

    /**
     * Indica se la Board non contiene nessun dado
     *
     * @return
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Restituisce il dado nella cella in posizione row,column
     * @param row riga della cella
     * @param column colonna della cella
     * @return dado nella cella selezionata
     * @throws NoDieException se non e' presente alcun dado nella cella
     */
    public Die getDie(int row, int column) throws NoDieException {
        return get(row, column).getDie();
    }

    /**
     * Controlla la presenza di un dado nella cella in posizione row,column
     * @param row riga della cella
     * @param column colonna della cella
     * @return true se la cella e' piena, false se vuota
     */
    public boolean containsDie(int row, int column) {
        return get(row, column).isUsed();
    }

    /**
     * Rimuove il dado nella cella in posizione row,column
     * @param row riga della cella
     * @param column colonna della cella
     * @throws NoDieException se la cella e' vuota
     */
    public void removeDie(int row, int column) throws NoDieException {
        get(row, column).removeDie();
    }

    /**
     * Inserisce un dado nella cella di posizione row,column
     * @param die dado da inserire
     * @param row riga della cella
     * @param column colonna della cella
     * @throws AlredySetDie se e' gia presente un dado nella cella
     */
    public void setDie(Die die, int row, int column) throws AlredySetDie {
        get(row, column).setDie(die);
        isEmpty = false;
    }

    /**
     * Verifica se il dado die rispetta la restrizione di colore della cella in posizione row,column
     * @param die dado da controllare
     * @param row riga della cella
     * @param column colonna della cella
     * @return esito della verifica: true se passata, false altrimenti
     */
    public boolean verifyColorRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isColorRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    /**
     * Verifica se il dado die rispetta le restrizione di numero della cella in posizione row,column
     * @param die dado da controllare
     * @param row riga della cella
     * @param column colonna della cella
     * @return esito della verifica: true se passata, false altrimenti
     */
    public boolean verifyNumberRestriction(Die die, int row, int column) {
        if (get(row, column).getRestriction().isNumberRestriction())
            return get(row, column).verifyRestriction(die);

        return true;
    }

    /**
     * Verifica le regole di posizione delle celle nella plancia
     * @param row riga della cella
     * @param column colonna della cella
     * @return esito della verifica: true se passata, false altrimenti
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
     * Verifica se il dado die rispetta le restrizione di prossimita' della cella in posizione row,column
     * @param die dado da controllare
     * @param row riga della cella
     * @param column colonna della cella
     * @return esito della verifica: true se passata, false altrimenti
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
