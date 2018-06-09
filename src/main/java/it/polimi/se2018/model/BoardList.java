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

            Object obj2 = parser.parse(new FileReader("test.json"));

            JSONObject jsonObject = (JSONObject) obj2;
            System.out.println(jsonObject);

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
        return boardList.get((new Random()).nextInt(boardList.size()));
    }
}
