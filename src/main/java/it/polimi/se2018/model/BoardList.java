package it.polimi.se2018.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alessio
 */
public final class BoardList {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private ArrayList<Board[]> boardList = new ArrayList<>();

    /**
     * Constructor, load all the maps from the json file
     */
    public BoardList(){
        JSONParser parser = new JSONParser();
        try {

            Object obj2 = parser.parse(new FileReader("./mappe.json"));

            JSONObject jsonObject = (JSONObject) obj2;
            JSONArray boards = (JSONArray) jsonObject.get("boards");
            JSONArray coppia;
            JSONObject board;
            JSONArray row;
            ArrayList<String> restrictions;
            Board[] coppiaBoard;
            for(int i = 0; i < boards.size(); i ++){
                coppia = (JSONArray)((JSONObject) boards.get(i)).get("coppia");
                coppiaBoard = new Board[2];
                for(int j = 0; j < 2; j++){
                    restrictions = new ArrayList<>();
                    board = (JSONObject)coppia.get(j);
                    for(int k = 0; k < 4; k++){
                        row = (JSONArray)((JSONArray) board.get("mappa")).get(k);
                        for(int l = 0; l < 5; l++){
                            restrictions.add(((String) row.get(l)));
                        }
                        coppiaBoard[j] = new Board((String) board.get("name"),(int) (long) board.get("difficulty"),restrictions);
                    }
                    boardList.add(coppiaBoard);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE,"File mappe.json non trovato");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Errore lettura mappe.json");
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE,"Errore parsing mappe.json");
        }
    }

    /**
     * Remove a couple of boards from the list of board
     * @return a couple of boards
     */
    public Board[] getCouple(){
        if(boardList.isEmpty()){
            LOGGER.log(Level.SEVERE,"Nessuna coppia di mappe disponibile");
            return null;
        }else{
            return boardList.remove((new Random()).nextInt(boardList.size()));
        }
    }

    /**
     * Return the board with the specified name without removing it from the list of boards
     * @param name the name of the board
     * @return the board
     */
    public Board getBoard(String name){
        for(int i = 0; i < boardList.size(); i ++){
            for(int j = 0; j < 2; j++) {
                if (boardList.get(i)[j].getName().equals(name)){
                    LOGGER.log(Level.FINE,"Nome board " + boardList.get(i)[j].getName());
                    return boardList.get(i)[j];
                }
            }
        }
        LOGGER.log(Level.WARNING,"Board non trovata");
        return null;
    }

    /**
     * Return the array of boards
     * @return the array of boards
     */
    public Board[] values(){
        ArrayList<Board> boardValues = new ArrayList<>();
        for(int i = 0; i < boardList.size(); i ++){
            boardValues.addAll(Arrays.asList(boardList.get(i)));

        }
        Board[] boardArray = new Board[boardValues.size()];
        return boardValues.toArray(boardArray);
    }

}
