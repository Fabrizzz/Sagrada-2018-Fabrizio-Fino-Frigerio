package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracciato dei dadi
 * @author Giampietro
 */
public class RoundTrack implements Serializable {
    private List<List<Die>> tracks = new ArrayList<>(10);   //Il tracciato è una lista di liste perchè a ogni round si possono scartare più dadi

    /**
     * Costruttore
     */
    public RoundTrack() {
        for (int i = 0; i < 10; i++) {
            tracks.add((new ArrayList<>()));    //Per ogni round si crea un arraylist contenente i dadi scartati
        }
    }

    /**
     * Estrae un dado dal tracciato nel giro round in posizione pos
     * @param round giro del dado
     * @param pos posizione del dado nel round
     * @return il dado del giro nella posizione pos
     */
    public Die getDie(int round, int pos) throws NoDieException{
        try{
            Die die = tracks.get(round).get(pos);
            return die;
        }catch (IndexOutOfBoundsException e){
            throw new NoDieException();
        }

    }

    /**
     * Aggiunge il dado scartato die al tracciato nel giro round
     * @param round giro di gioco
     * @param die dado da scartare
     */
    public void addDie(int round, Die die) {
        tracks.get(round).add(die);
    }

    /**
     * Aggiunge i dadi scartati dice al tracciato nel giro round
     * @param round giro di gioco
     * @param dice dadi da scartare
     */
    public void addDice(int round, List<Die> dice) {
        tracks.get(round).addAll(dice);
    }

    /**
     * Rimuove il dado scartato in posizione pos nel giro round
     * @param round turno di gioco
     * @param pos posizione del dado nel giro
     * @throws NoDieException se non e' presente il dado
     */
    public void removeDie(int round, int pos) throws NoDieException {
        if (pos >= tracks.get(round).size())
            throw new NoDieException();
        tracks.get(round).remove(pos);
    }

    /**
     * Numero di dadi scartati nel giro round
     * @param round turno di gioco
     * @return numero di dadi scartati nel giro
     */
    public int numberOfDice(int round) {
        return tracks.get(round).size();
    }

    /**
     * Ritorna la presenza del colore color ne tracciato
     * @param color colore da cercare
     * @return true se il colore e' prensente in almento un dado, false altrimenti
     */
    public boolean hasColor(Color color) {
        for (List<Die> track : tracks) {
            for (Die die : track) {
                if (die.getColor().equals(color))
                    return true;
            }
        }
        return false;
    }
}
