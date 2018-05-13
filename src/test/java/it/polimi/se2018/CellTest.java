package it.polimi.se2018;

import it.polimi.se2018.model.cell.*;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CellTest {
    private Cell cell;
    private Die dice;

    @Before
    public void initCell(){
        cell = new Cell(new NoRestriction());
        dice = new Die(Color.BLUE);
    }

    @Test
    public void testIsUsed(){
        assertFalse(cell.isUsed());

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
        try{
            cell.removeDie();
            fail();
        }catch(NoDieException e){
            assertFalse(cell.isUsed());
        }

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
        try{
            cell.getDie();
            fail();
        }catch(NoDieException e){
            assertFalse(cell.isUsed());
        }

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
