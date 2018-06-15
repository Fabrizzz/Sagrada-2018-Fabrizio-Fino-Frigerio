package it.polimi.se2018.View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Controller GUI Socket
 * @author Matteo
 */
public class ControllerGUISocket implements Initializable {

    private static final int limitIPSocket= 15;
    private static final int limitIPPort= 5;

    @FXML
    private Label labelIPSocket;

    @FXML
    private Label labelPortaSocket;

    @FXML
    private Label labelNickSocket;

    @FXML
    private TextField textIPSocket;

    @FXML
    private TextField textPortaSocket;

    @FXML
    private TextField textNickSocket;

    @FXML
    private Button buttonIndietroSocket;

    @FXML
    private Button buttonAvantiSocket;

    public void handleButtonIndietroSocket(ActionEvent event) {
        Stage stage;
        Parent newScene;
        Scene scene = null;

        stage = (Stage) buttonIndietroSocket.getScene().getWindow();
        try{
            newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));
            scene = new Scene(newScene);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        stage.setTitle("Sagrada");
        stage.setScene(scene);
        stage.show();
    }

    public void handleButtonAvantiSocket(ActionEvent event) {

        createConnection();
        /*
        Aggiungere controllo se la connessione è riuscita o fallita
         */

        Stage stage;
        Parent newScene;
        Scene scene = null;

        stage = (Stage) buttonAvantiSocket.getScene().getWindow();
        try{
            newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlWaiting.fxml"));
            scene = new Scene(newScene);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        stage.setTitle("Preparazione gioco");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Max length textfield ip
        textIPSocket.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    if (textIPSocket.getText().length() >= limitIPSocket ){
                        textIPSocket.setText(textIPSocket.getText().substring(0, limitIPSocket));
                    }
                }

            }
        });

        //Max length textfield port
        textPortaSocket.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    if (textPortaSocket.getText().length() >= limitIPPort ){
                        textPortaSocket.setText(textPortaSocket.getText().substring(0, limitIPPort));
                    }
                }

            }
        });
    }




    public void createConnection(){

        /*
        Scrivere questo metodo (e anche in ControllerGUIRMI che crea la connessione; capire come passare il ClientNetwork che sta in GUIProxy


        ClientNetwork clientNetwork = new ClientNetwork( ... ci va la View ...);

        Long localID;
        String address = "";
        int port = 0;
        String nick = "";

        //SOCKET
        while(!clientNetwork.isConnected()) {
            address = textIPSocket.getText();
            port = Integer.parseInt(textPortaSocket.getText());
            clientNetwork.connectSocket(address, port);
        }
        System.out.println("Connessione accettata");

        //RMI (Da mettere in RMI)
        while(!clientNetwork.isConnected()) {
            address = textIPSocket.getText();
            clientNetwork.connectRMI(address);
        }
        System.out.println("Connessione accettata");

        //NICKNAME
        nick = textNickSocket.getText();

        localID = (new Random()).nextLong();

        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            System.out.println("Nome utente inviato");
        }else{
            System.out.println("Errore connessione");
        }

        ClientMessage testMessage = new ClientMessage(new PlayerMove(Tool.SKIPTURN));
        clientNetwork.sendMessage(testMessage);

        */
    }




}
