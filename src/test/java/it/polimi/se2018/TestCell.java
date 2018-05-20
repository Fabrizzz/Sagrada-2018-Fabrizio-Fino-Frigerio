package it.polimi.se2018;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.Restriction;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCell {
    private Cell cell;
    AbstractRestrictionFactory factory;
    private Die die;

    @Before
    public void initCell(){
        die = new Die(Color.BLUE);
        factory = AbstractRestrictionFactory.getFactory();
        cell = new Cell(factory.createNoRestriction());
    }

    @Test
    public void testIsUsed(){
        assertFalse(cell.isUsed());

        try{
            cell.setDie(die);
        }catch (AlredySetDie e){
            fail();
        }
        assertTrue(cell.isUsed());

        try{
            cell.setDie(die);
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
            cell.setDie(die);
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
            cell.setDie(die);
        }catch (AlredySetDie e){
            fail();
        }

        try {
            assertEquals(cell.getDie(), die);
        }catch (NoDieException e){
            fail();
        }
    }

    @Test
    public void testSetDice(){
        try{
            cell.setDie(die);
        }catch (AlredySetDie e){
            fail();
        }

        try{
            cell.setDie(die);
            fail();
        }catch (AlredySetDie e){}
    }

    @Test
    public void testVerifyRestriction(){

        Cell cell = new Cell(factory.createColorRestriction(Color.BLUE));

        assertTrue(cell.verifyRestriction(die));

        die = new Die(Color.RED);
        assertFalse(cell.verifyRestriction(die));

    }

    @Test
    public void testGetRestriction(){
        Restriction restriction = factory.createNoRestriction();
        Cell cell = new Cell(restriction);

        assertEquals(cell.getRestriction(),restriction);
    }
}
