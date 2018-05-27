package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;

public class PublicObjectiveFactory {

    public static PublicObjective createPublicObjective(PublicObjectiveName name) {
        if (name == PublicObjectiveName.COLORIDIVERSIRIGA)
            return new Card1RowColorVariety();
        else if (name == PublicObjectiveName.COLORIDIVERSICOLONNA)
            return new Card2ColumnColorVariety();
        else if (name == PublicObjectiveName.SFUMATUREDIVERSERIGA)
            return new Card3RowShadeVariety();
        else if (name == PublicObjectiveName.SFUMATUREDIVERSECOLONNA)
            return new Card4ColumnShadeVariety();
        else if (name == PublicObjectiveName.SFUMATUREDIVERSE)
            return new Card8ShadeVariety();
        else if (name == PublicObjectiveName.DIAGONALICOLORATE)
            return new Card9ColorDiagonals();
        else if (name == PublicObjectiveName.VARIETADICOLORE)
            return new Card10ColorVariety();

        return new CardsShades(name);

    }
}
