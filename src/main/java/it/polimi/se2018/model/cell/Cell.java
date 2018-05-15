package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;

/**
 * Classe cella della plancia
 * @author Giampietro
 */
public class Cell implements Serializable {
    private final Restriction restriction;
    private Die die;
    private boolean isUsed = false;

    /**
     * Costruttore
     * @param restriction restrizione da applicare alla cella
     */
    public Cell(Restriction restriction) {
        this.restriction = restriction;
    }

    /**
     * Ritorna se nella cella c'e' un dado.
     * @return Boolean utilizzo cella
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Rimuove il dado dalla cella
     * @throws NoDieException se la cella e' vuota
     */
    public void removeDie() throws NoDieException {

        if (!isUsed())
            throw new NoDieException();

        isUsed = false;
    }

    /**
     * Ritorna il dado presente nella cella
     * @return il dado della cella
     * @throws NoDieException se la cella e' vuota
     */
    public Die getDie() throws NoDieException {
        if (!isUsed())
            throw new NoDieException();
        return die;
    }

    /**
     * Inserisce un dado nella cella
     * @param die dado da inserire
     * @throws AlredySetDie se e' gia presente un dado nella cella
     */
    public void setDie(Die die) throws AlredySetDie {
        if (isUsed)
            throw new AlredySetDie();
        this.die = die;
        isUsed = true;
    }

    /**
     * Verifica se il dado rispetta le restrizioni della cella
     * @param die dado da verificare
     * @return se il dado rispetta le restrizioni
     */
    public boolean verifyRestriction(Die die) {
        return restriction.verifyRestriction(die);
    }

    /**
     * Restituisce la restrizione presente nella cella
     * @return la restrizione della cella
     */
    public Restriction getRestriction() {
        return restriction;
    }


}
