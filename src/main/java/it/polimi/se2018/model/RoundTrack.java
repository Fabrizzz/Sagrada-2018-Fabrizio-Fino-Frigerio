package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class RoundTrack {
    private List<List<Die>> tracks = new ArrayList<>(10);

    public RoundTrack() {
        for (int i = 0; i < 10; i++) {
            tracks.add((new ArrayList<>()));
        }
    }

    public Die getDie(int round, int pos) {
        return tracks.get(round).get(pos);
    }

    public void addDie(int round, Die die) {
        tracks.get(round).add(die);
    }

    public int numberOfDice(int round) {
        return tracks.get(round).size();
    }

    public boolean hasColor(Color color) {  //utile per una toolcard
        for (List<Die> track : tracks) {
            for (Die die : track) {
                if (die.getColor().equals(color))
                    return true;
            }
        }
        return false;
    }
}
