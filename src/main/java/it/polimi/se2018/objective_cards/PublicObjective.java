package it.polimi.se2018.objective_cards;

public abstract class PublicObjective implements ObjectiveCard {
    private PublicObjectiveName objectiveName;

    public PublicObjective(PublicObjectiveName objectiveName){
        this.objectiveName = objectiveName;
    }

    public PublicObjectiveName getObjectiveName() {
        return objectiveName;
    }
}
