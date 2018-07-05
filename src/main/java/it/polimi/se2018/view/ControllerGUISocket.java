package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.InputUtils;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.ServerMessage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI Socket
 * @author Matteo
 */
public class ControllerGUISocket implements Initializable {

    private static final int limitIPSocket= 15;
    private static final int limitIPPort= 5;

    private ClientNetwork clientNetwork;
    private Long localID;
    private ServerMessage message;
    private ControllerWaiting nextController;
    private IntegerProperty num;


    @FXML
    private TextField textIPSocket;

    @FXML
    private TextField textPortaSocket;

    @FXML
    private TextField textNickSocket;

    @FXML
    private Button buttonAvantiSocket;

    public void handleButtonAvantiSocket(ActionEvent event) {

        Stage stage;
        Parent newScene;
        Scene scene = null;
        FXMLLoader loader = null;
        boolean connect;

        connect = createConnection();

        if(connect == false){
            popupError();
        }
        else {
            stage = (Stage) buttonAvantiSocket.getScene().getWindow();
            try {
                loader = new FXMLLoader(getClass().getResource("/fxmlFile/fxmlWaiting.fxml"));
                newScene = (Parent) loader.load();
                scene = new Scene(newScene);
            } catch (Exception e) {
                System.out.println("File FXML not found");
            }
            nextController = loader.getController();
            nextController.sendInfo(message, num);
            stage.setTitle("Preparazione gioco");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
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




    public boolean createConnection(){

        String ip;
        int port;
        String nick;
        boolean bool = true;

        ip = textIPSocket.getText();
        nick = textNickSocket.getText();


        localID = JSONUtils.readID(nick);

        try{
            port = Integer.parseInt(textPortaSocket.getText());
        }catch (Exception e){
            System.out.println("Input non corretto, inserire un numero");
            port = 0;
            bool = false;
        }

        if(Server.available(port)){
            bool = false;
            System.out.println("Porta non disponibile");
        }

        if(!clientNetwork.connectSocket(ip, port)){
            System.out.println("Connessione non riuscita; Ip o porta errati");
            bool = false;
        }

        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(!clientNetwork.sendMessage(clientMessage)){
            bool = false;
            System.out.println("Errore nell'invio del nome");
        }

        return bool;
    }

    public void popupError(){

            Stage popupStage= new Stage();
            Label label;
            VBox layout = new VBox(10);
            Scene scene = new Scene(layout, 250, 100);

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Errore");

            label = new Label("Errore nell'inserimento. Riprova!");
            label.setFont(Font.font("System", 16));
            //label.setStyle("-fx-font-weight: bold");
            layout.getChildren().add(label);
            layout.setAlignment(Pos.CENTER);
            popupStage.setScene(scene);
            popupStage.setMinWidth(250);
            popupStage.setMinHeight(100);
            popupStage.showAndWait();
    }

    public void sendInfo(ClientNetwork clientNetwork, ServerMessage message, IntegerProperty num) {
        this.clientNetwork = clientNetwork;
        this.message = message;
        this.num = num;
    }

    /*
    //Tasto indietro: eliminato per problemi con clientNetwork
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
     */

}
