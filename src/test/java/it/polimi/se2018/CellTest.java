package it.polimi.se2018;

import it.polimi.se2018.model.cell.*;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CellTest {


    @Test
    public void testIsUsed(){
        Cell cell = new Cell(new NoRestriction());
        assertFalse(cell.isUsed());
        Die dice = new Die(Color.BLUE);

        try{
            cell.setDie(dice);
        }catch (AlredySetDie e){
            fail();
        }
        assertTrue(cell.isUsed());

        try{
            cell.setDie(dice);
            fail();
        }catch (AlredySetDie e){
            assertTrue(cell.isUsed());
        }
    }

    @Test
    public void testRemoveDie(){
        Cell cell = new Cell(new NoRestriction());
        try{
            cell.removeDie();
            fail();
        }catch(NoDieException e){
            assertFalse(cell.isUsed());
        }

        Die dice = new Die(Color.BLUE);
        try{
            cell.setDie(dice);
        }catch (AlredySetDie e){
            fail();
        }

        assertTrue(cell.isUsed());

        try{
            cell.removeDie();
        }catch(NoDieException e){
            fail();
        }

        assertFalse(cell.isUsed());
    }

    @Test
    public void testGetDice(){
        Cell cell = new Cell(new NoRestriction());
        try{
            cell.getDie();
            fail();
        }catch(NoDieException e){
            assertFalse(cell.isUsed());
        }

        Die dice = new Die(Color.BLUE);
        try{
            cell.setDie(dice);
        }catch (AlredySetDie e){
            fail();
        }

        try {
            assertEquals(cell.getDie(), dice);
        }catch (NoDieException e){
            fail();
        }
    }

    @Test
    public void testSetDice(){
        Cell cell = new Cell(new NoRestriction());
        Die dice = new Die(Color.BLUE);

        try{
            cell.setDie(dice);
        }catch (AlredySetDie e){
            fail();
        }

        try{
            cell.setDie(dice);
            fail();
        }catch (AlredySetDie e){}
    }

    @Test
    public void testVerifyRestriction(){
        Restriction restriction = new ColorRestriction(Color.BLUE);
        Cell cell = new Cell(restriction);
        Die dice = new Die(Color.BLUE);

        try{
            cell.setDie(dice);
        }catch(AlredySetDie e){
            fail();
        }

        assertTrue(cell.verifyRestriction(dice));

        try{
            cell.removeDie();
        }catch(NoDieException e){
            fail();
        }

        dice = new Die(Color.RED);
        assertFalse(cell.verifyRestriction(dice));

    }

    @Test
    public void testGetRestriction(){
        Restriction restriction = new NoRestriction();
        Cell cell = new Cell(restriction);

        assertEquals(cell.getRestriction(),restriction);
    }
}
