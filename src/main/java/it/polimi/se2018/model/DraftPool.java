package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DraftPool implements Serializable {

    private List<Die> dice = new ArrayList<>(9);    //Lista contenente tutti i dadi pescati
    private int numberOfDices;  //Numero di dadi da pescare a ogni round

    //Costruttore
    public DraftPool(int lenght, DiceBag diceBag) {
        numberOfDices = lenght * 2 + 1; //Inizializzazione del numero di dadi da pescare ogni round
        for (int i = 0; i < numberOfDices; i++)
            addDie(diceBag.takeDie());  //Si pescano i dadi dal sacchetto
    }

    //A ogni round si fa una nuova pescata e si cambiano tutti i dadi
    public void rollDice(DiceBag diceBag) {
        for (int i = 0; i < numberOfDices; i++) {
            dice.add(diceBag.takeDie());
        }
    }

    //Roll a tutti i dadi della draftpool
    public void reRollDice() {
        for (Die die : dice) {
            die.reRoll();
        }
    }

    //Restituisce il dado nella posizione indicata
    public Die getDie(int i) throws NoDieException {
        if (i >= size())
            throw new NoDieException();
        return dice.get(i);
    }

    //Aggiunge un dado alla lista
    public void addDie(Die die) {
        dice.add(die);
    }

    //Rimozione di un dado per indice
    public void removeDie(int i) {
        dice.remove(i);
    }

    //Rimuove il dado passato per parametro
    public void removeDie(Die die) {
        dice.remove(die);
    }

    //Rimuove tutti i dadi
    public List<Die> removeAll() {
        List<Die> temp = new ArrayList<>(dice);
        dice.clear();
        return temp;

    }

    //Ritorna la dimensione attuale di dice (dei dadi rimasti tra quelli pescati)
    public int size() {
        return dice.size();
    }

}
