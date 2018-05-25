package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.NoDieException;


/**
 * Obiettivo pubblico 5 - 6 -7
 * @author Matteo
 */
public class CardsShades extends PublicObjective {

    NumberEnum first;
    NumberEnum second;

    /**
     * Costruttore
     * @param objectiveName
     */
    protected CardsShades(PublicObjectiveName objectiveName) {
        super(objectiveName);
        if (objectiveName == PublicObjectiveName.SFUMATURECHIARE) {
            first = NumberEnum.ONE;
            second = NumberEnum.TWO;
        } else if (objectiveName == PublicObjectiveName.SFUMATUREMEDIE) {
            first = NumberEnum.THREE;
            second = NumberEnum.FOUR;
        } else if (objectiveName == PublicObjectiveName.SFUMATURESCURE) {
            first = NumberEnum.FIVE;
            second = NumberEnum.SIX;
        } else
            throw new IllegalArgumentException();

    }

    /**
     * Conta i punti: 2 punti per ogni coppia di 1 e 2
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int contatore1 = 0;
        int contatore2 = 0;
        NumberEnum temp;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5; column++) {
                try {
                    temp = playerBoard.getDie(row, column).getNumber();
                    if (temp.equals(first)) {
                        contatore1++;
                    } else if (temp.equals(second)) {
                        contatore2++;
                    }
                } catch (NoDieException e) {
                    //ignore
                }
            }

        }

        return 2 * Math.min(contatore1, contatore2);
    }
}
