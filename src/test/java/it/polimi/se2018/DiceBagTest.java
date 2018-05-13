package it.polimi.se2018;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceBagTest {

    @Test
    public void takeDie() {
        int j = 18;
        int i = 0;
        DiceBag dicebag = new DiceBag(j);
        assertEquals(Color.values().length * j, dicebag.size());
        while(dicebag.size() != 0){
            if(dicebag.takeDie().getColor() == Color.BLUE){
                i++;
            }
        }
        assertEquals(18,i);
    }

    @Test
    public void addDie() {
        int j = 18;
        int i = 0;
        int k;
        DiceBag dicebag = new DiceBag(j);
        Die dice = new Die(Color.BLUE);
        k = dicebag.size();
        dicebag.addDie(dice);
        assertEquals(k + 1, dicebag.size());
        while(dicebag.size() != 0){
            if(dicebag.takeDie().getColor() == Color.BLUE){
                i++;
            }
        }
        assertEquals(19,i);

    }

    @Test
    public void size() {
        int j = 18;
        int i = 0;
        int k;
        DiceBag dicebag = new DiceBag(j);
        k = dicebag.size();
        while(dicebag.size() != 0){
                dicebag.takeDie();
                i++;
            }
        assertEquals(i, k);
    }
}