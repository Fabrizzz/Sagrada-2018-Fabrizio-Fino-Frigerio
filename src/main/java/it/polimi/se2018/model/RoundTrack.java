package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoundTrack implements Serializable {
    private List<List<Die>> tracks = new ArrayList<>(10);   //Il tracciato è una lista di liste perchè a ogni round si possono scartare più dadi

    //Costruttore
    public RoundTrack() {
        for (int i = 0; i < 10; i++) {
            tracks.add((new ArrayList<>()));    //Per ogni round si crea un arraylist contenente i dadi scartati
        }
    }

    //Si prende un dado indicando il round e la posizione tra i dadi scartati in quel round
    public Die getDie(int round, int pos) {
        return tracks.get(round).get(pos);
    }

    //Aggiunta del dado scartato
    public void addDie(int round, Die die) {
        tracks.get(round).add(die);
    }

    public void removeDie(int round, int pos) throws NoDieException {
        if (pos >= tracks.get(round).size())
            throw new NoDieException();
        tracks.get(round).remove(pos);
    }

    //Numero di dadi scartati in un determinato round
    public int numberOfDice(int round) {
        return tracks.get(round).size();
    }

    //Cerca il colore passato nel tracciato; utili per una toolcard
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
