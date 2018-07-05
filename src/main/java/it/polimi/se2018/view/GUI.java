package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.utils.messages.ServerMessage;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

/**
 * GUI
 * @author Matteo
 */
public class GUI extends Application {

    private static final CountDownLatch latch = new CountDownLatch(1);
    private static GUI classe = null;
    private ClientNetwork clientNetwork;
    private ServerMessage message;
    private ControllerGUI nextController;
    private IntegerProperty num;

    public static GUI waitStartUpGUI() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Errore");
            Thread.currentThread().interrupt();
        }
        return classe;
    }

    public static void setStartUpGUI(GUI startUp) {
        classe = startUp;
        latch.countDown();
    }

    public void sendInfo(ClientNetwork clientNetwork, ServerMessage message, IntegerProperty num) {
        this.clientNetwork = clientNetwork;
        this.message = message;
        this.num = num;
    }

    public GUI() {
        setStartUpGUI(this);
    }

    public void messageStartUpGUI() {
        System.out.println("Avvio della GUI");
    }

    /**
     * Contains the code for the JavaFX Application
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = null;
        Parent root;
        Scene scene = null;
        try{
            loader = new FXMLLoader(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));
            root = (Parent) loader.load();
            scene = new Scene(root,600,400);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }

        nextController = loader.getController();
        nextController.sendInfo(clientNetwork, message, num);

        primaryStage.setTitle("Sagrada");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /*
    public static void main(String[] args) {
        launch(args);
    }
    */
}
