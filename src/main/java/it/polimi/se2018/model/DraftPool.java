package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Riserva dei dadi del turno
 * @author Giampietro
 */
public class DraftPool implements Serializable {

    private List<Die> dice = new ArrayList<>(9);    //Lista contenente tutti i dadi pescati
    private int numberOfDices;  //Numero di dadi da pescare a ogni round

    /**
     * Costruttore
     * @param lenght
     * @param diceBag Sacchetto dei dadi
     */
    public DraftPool(int lenght, DiceBag diceBag) {
        numberOfDices = lenght * 2 + 1; //Inizializzazione del numero di dadi da pescare ogni round
        for (int i = 0; i < numberOfDices; i++)
            addDie(diceBag.takeDie());  //Si pescano i dadi dal sacchetto
    }

    /**
     * Estrae i dadi dal sacchetto e gli inserisce nella riserva
     * @param diceBag Sacchetto dei dadi
     */
    public void rollDice(DiceBag diceBag) {
        for (int i = 0; i < numberOfDices; i++) {
            dice.add(diceBag.takeDie());
        }
    }

    /**
     * Ritira tutti i dadi nella riserva
     */
    public void reRollDice() {
        for (Die die : dice) {
            die.reRoll();
        }
    }

    /**
     * Restituisce il dado nella riserva in posizione i
     * @param i posizione del dado
     * @return dado nella posizione i
     * @throws NoDieException se non e' presente nessun dado nella posizione i
     */
    public Die getDie(int i) throws NoDieException {
        if (i >= size())
            throw new NoDieException();
        return dice.get(i);
    }

    /**
     * Aggiunge un dado alla riserva
     * @param die dado da aggiungere
     */
    public void addDie(Die die) {
        dice.add(die);
    }

    /**
     * Rimuove il dado dalla rieserva nell'indice i
     * @param i indice del dado da rimuovere
     */
    public void removeDie(int i) {
        dice.remove(i);
    }

    /**
     * Rimuove il dado die dalla riserva
     * @param die dado da rimuovere
     */
    public void removeDie(Die die) throws NoDieException {
        if (!dice.contains(die))
            throw new NoDieException();
        dice.remove(die);//TODO aggiungere eccezione nel caso il dado non sia presente
    }

    /**
     * Riumuove tutti i dadi dalla riserva
     * @return ritorna la copia della lista dei dadi prima della rimozione
     */
    public List<Die> removeAll() {
        List<Die> temp = new ArrayList<>(dice);
        dice.clear();
        return temp;

    }

    /**
     * Restituisce il numero dei dadi nella riserva
     * @return numero dei dadi nella riserva
     */
    public int size() {
        return dice.size();
    }

}
