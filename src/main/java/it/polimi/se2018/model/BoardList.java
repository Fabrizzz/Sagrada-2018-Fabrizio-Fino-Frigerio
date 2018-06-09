package it.polimi.se2018.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public final class BoardList {

    private BoardList(){}

    private static ArrayList<Board[]> boardList = new ArrayList<Board[]>();


    public static void loadJSONBoards(){
        JSONParser parser = new JSONParser();
        try {

            Object obj2 = parser.parse(new FileReader("mappe.json"));

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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Board[] getCouple(){
        if(boardList.size() == 0){
            return null;
        }else{

            return boardList.remove((new Random()).nextInt(boardList.size()));
        }
    }

    public static Board getBoard(String name){
        for(int i = 0; i < boardList.size(); i ++){
            for(int j = 0; j < 2; j++) {
                if (boardList.get(i)[j].getName().equals(name)){
                    return boardList.get(i)[j];
                }
            }
        }
        return null;
    }

    public static Board[] values(){
        ArrayList<Board> boardValues = new ArrayList<>();
        for(int i = 0; i < boardList.size(); i ++){
            for(int j = 0; j < 2; j++){
                boardValues.add(boardList.get(i)[j]);
            }
        }

        Board[] boardArray = new Board[boardValues.size()];
        boardArray = boardValues.toArray(boardArray);

        return boardArray;
    }

}
