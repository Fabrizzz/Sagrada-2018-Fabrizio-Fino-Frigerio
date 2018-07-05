package it.polimi.se2018.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONUtils {
    private static final Logger LOGGER = Logger.getLogger("Logger");

    private JSONUtils(){}

    /**
     * Read the id of the player nick from the settings.json file
     * @param nick nickname of the player
     * @return the id of the player
     */
    public static Long readID(String nick){
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("settings.json"));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray players = (JSONArray) jsonObject.get("players");
            JSONObject player;
            String playerNick;
            for(int i = 0; i < players.size(); i ++){
                player = (JSONObject) players.get(i);
                playerNick = (String) player.get("nickname");
                if(playerNick.equals(nick)){
                    return (Long) player.get("id");
                }
            }

            player = new JSONObject();
            player.put("nickname" , nick);
            Long id = generateID();
            player.put("id",id);
            players.add(player);

            JSONObject wObj = new JSONObject();
            if((Long) ((JSONObject) obj).get("timerTurno") != null)
                wObj.put("timerTurno" , (Long) ((JSONObject) obj).get("timerTurno"));
            else
                wObj.put("timerTurno" , 1);

            if((Long) ((JSONObject) obj).get("timerConnessioni") != null)
                wObj.put("timerConnessioni" , (Long) ((JSONObject) obj).get("timerConnessioni"));
            else
                wObj.put("timerConnessioni" , 60);

            wObj.put("players",players);
            writeJSON(wObj);
            return id;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.FINE,"File settings.json non trovato");
            try{
                JSONObject obj = new JSONObject();
                obj.put("timerTurno",1);
                obj.put("timerConnessioni", 60);

                JSONArray list = new JSONArray();
                JSONObject player = new JSONObject();
                player.put("nickname",nick);
                Long id = generateID();
                player.put("id",id);
                list.add(player);
                obj.put("players", list);

                writeJSON(obj);
                return id;
            }catch (Exception g){
                LOGGER.log(Level.WARNING, "Errore scrittura file json");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Errore lettura settings.json");
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE,"Errore parsing settings.json");
        }
        LOGGER.log(Level.WARNING,"Failback generazione id");
        return generateID();
    }

    /**
     * Read the round timer from the setting.json file
     * @return the roundTimer
     */
    public static int readRoundTimer(){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("settings.json"));

            JSONObject jsonObject = (JSONObject) obj;

            if(jsonObject.get("timerTurno") != null){
                LOGGER.log(Level.FINE,"Timer round letto da file");
                return ((Long) jsonObject.get("timerTurno")).intValue();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.FINE,"File settings.json non trovato");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Errore lettura settings.json");
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING,"Errore parsing settings.json");
        }
        LOGGER.log(Level.WARNING,"Failback generazione roundTimer");
        return 1;
    }

    /**
     * Read the round timer from the setting.json file
     * @return the connectionTimer
     */
    public static int readConnectionTimer(){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("settings.json"));

            JSONObject jsonObject = (JSONObject) obj;

            if(jsonObject.get("timerConnessioni") != null){
                LOGGER.log(Level.FINE,"Timer connessione letto da file");
                return ((Long) jsonObject.get("timerConnessioni")).intValue();
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.FINE,"File settings.json non trovato");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Errore lettura settings.json");
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING,"Errore parsing settings.json");
        }
        LOGGER.log(Level.WARNING,"Failback generazione roundTimer");
        return 60;
    }

    /**
     * Write a json object to file
     * @param obj json object
     */
    private static void writeJSON(JSONObject obj){
        try (FileWriter file = new FileWriter("settings.json")) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException h) {
            LOGGER.log(Level.WARNING,"Errore scrittura file json");
        }
    }

    /**
     * Generate the id
     * @return the id
     */
    private static Long generateID(){
        return (new Random()).nextLong();
    }
}
