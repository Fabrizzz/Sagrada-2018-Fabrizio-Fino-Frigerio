package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.server.Server;
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
 * Controller GUI RMI
 * @author Matteo
 */
public class ControllerGUIRMI implements Initializable {

    private static final int limitIPRMI= 15;
    private ClientNetwork clientNetwork;
    private Long localID;
    private ServerMessage message;
    private IntegerProperty num;

    @FXML
    private TextField textIPRMI;

    @FXML
    private TextField textNickRMI;


    @FXML
    private Button buttonAvantiRMI;

    public void handleButtonAvantiRMI(ActionEvent event) {

        /*

        Sistemare tutta la parte di collegamento al server

         */

        Stage stage;
        Parent newScene;
        Scene scene = null;
        boolean connect;

        connect = createConnection();

        if(connect == false){
            popupError();
        }
        else {
            stage = (Stage) buttonAvantiRMI.getScene().getWindow();
            try {
                newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlWaiting.fxml"));
                scene = new Scene(newScene);
            } catch (Exception e) {
                System.out.println("File FXML not found");
            }
            stage.setTitle("Preparazione gioco");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Max length textfield ip
        textIPRMI.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    if (textIPRMI.getText().length() >= limitIPRMI ){
                        textIPRMI.setText(textIPRMI.getText().substring(0, limitIPRMI));
                    }
                }

            }
        });

    }

    public boolean createConnection(){

        String ip;
        String nick;
        boolean bool = true;

        ip = textIPRMI.getText();
        nick = textNickRMI.getText();


        localID = JSONUtils.readID(nick);

        if(!clientNetwork.connectRMI(ip)){
            System.out.println("Connessione non riuscita; Ip errati");
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

}
