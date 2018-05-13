package it.polimi.se2018;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Cell;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Assert;
import org.junit.Test;

import java.util.prefs.NodeChangeEvent;

import static org.junit.Assert.*;

public class PlayerBoardTest {

    @Test
    public void testGetDie(){
        PlayerBoard playerBoard = new PlayerBoard();
        Die dice = new Die(Color.BLUE);

        try{
            playerBoard.getDie(0,0);
            fail();
        }catch(NoDieException e){}


        try{
            playerBoard.setDie(dice,0,0);
        }catch(AlredySetDie e){
            fail();
        }

        try{
            assertEquals(playerBoard.getDie(0,0),dice);
        }catch(NoDieException e){
            fail();
        }

    }

    @Test
    public void testContainsDie(){
        PlayerBoard playerBoard = new PlayerBoard();
        assertFalse(playerBoard.containsDie(0,0));


        Die dice = new Die(Color.BLUE);
        try{
            playerBoard.setDie(dice,0,0);
        }catch(AlredySetDie e){
            fail();
        }

        assertTrue(playerBoard.containsDie(0,0));
    }

    @Test
    public void testRemoveDice(){
        PlayerBoard playerBoard = new PlayerBoard();

        try{
            playerBoard.removeDie(0,0);
            fail();
        }catch(NoDieException e){}

        Die dice = new Die(Color.BLUE);
        try{
            playerBoard.setDie(dice,0,0);
        }catch(AlredySetDie e){
            fail();
        }

        try{
            playerBoard.removeDie(0,0);
        }catch(NoDieException e){
            fail();
        }

        try{
            playerBoard.getDie(0,0);
            fail();
        }catch(NoDieException e){}

    }

    @Test
    public void testVerifyColorRestriction(){
        //da fare dopo l'implementazione completa del costruttore di PlayerBoard
    }

    @Test
    public void testVerifyNumberRestriction(){
        //da fare dopo l'implementazione completa del costruttore di PlayerBoard
    }

    @Test
    public void testVerifyPositionRestriction(){
        //da fare dopo l'implementazione completa del costruttore di PlayerBoard
    }

    @Test
    public void testVerifyNearCellsRestriction(){
        //da fare dopo l'implementazione completa del costruttore di PlayerBoard
    }

    @Test
    public void testInitialPositionRestriction(){
        //da fare dopo l'implementazione completa del costruttore di PlayerBoard
    }

}
