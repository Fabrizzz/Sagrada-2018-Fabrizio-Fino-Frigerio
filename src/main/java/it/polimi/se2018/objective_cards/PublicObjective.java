package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;

public class PublicObjective implements ObjectiveCard {
    private PublicObjectiveName objectiveName;
    public PublicObjective(PublicObjectiveName objectiveName){
        this.objectiveName = objectiveName;
    };

    public int getPoints(PlayerBoard playerBoard){
        return 0;//da scrivere
    }

    public PublicObjectiveName getObjectiveName() {
        return objectiveName;
    }
}
