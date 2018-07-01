package it.polimi.se2018.view;

import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.*;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGame implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    HBox hbox;

    @FXML
    VBox vboxBoard;

    @FXML
    VBox vboxCard;

    @FXML
    HBox hboxLabel;

    @FXML
    HBox hboxBottoni;

    @FXML
    GridPane gridPane;

    @FXML
    HBox hboxTool;

    @FXML
    HBox hboxObjective;

    @FXML
    Pane paneTool1;

    @FXML
    Pane paneTool2;

    @FXML
    Pane paneObjective1;

    @FXML
    Pane paneObjective2;

    @FXML
    ImageView tool1;

    @FXML
    ImageView tool2;

    @FXML
    ImageView objective1;

    @FXML
    ImageView objective2;

    @FXML
    Button buttonTool1;

    @FXML
    Button buttonTool2;

    @FXML
    Button buttonZoomTool1;

    @FXML
    Button buttonZoomTool2;

    @FXML
    Label privateObjective;

    @FXML
    Separator separatorGrid1;

    @FXML
    Separator separatorGrid2;


    PlayerBoard playerBoard; // Messa globale per provare i metodi; in seguito da togliere perchè viene richiesta al server

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLayoutResizable();
        initializeRestrictionPlayerBoard();
        initializePrivateObjective();

        //Prova per la rimozione
        removeDie(0,3);
        removeDie(0,4);

    }

    private void initializeLayoutResizable() {

        hbox.prefHeightProperty().bind(root.heightProperty());
        hbox.prefWidthProperty().bind(root.widthProperty());
        vboxBoard.prefHeightProperty().bind(root.heightProperty());
        vboxBoard.prefWidthProperty().bind(root.widthProperty());
        vboxCard.prefHeightProperty().bind(root.heightProperty());
        vboxCard.prefWidthProperty().bind(root.widthProperty());
        hboxLabel.prefWidthProperty().bind(root.widthProperty());
        hboxBottoni.prefWidthProperty().bind(root.widthProperty());

        separatorGrid1.prefHeightProperty().bind(vboxBoard.heightProperty());
        separatorGrid2.prefHeightProperty().bind(vboxBoard.heightProperty());

        paneTool1.prefHeightProperty().bind(vboxCard.heightProperty());
        paneTool1.prefWidthProperty().bind(vboxCard.widthProperty());
        paneTool2.prefHeightProperty().bind(vboxCard.heightProperty());
        paneTool2.prefWidthProperty().bind(vboxCard.widthProperty());

        paneObjective1.prefHeightProperty().bind(vboxCard.heightProperty());
        paneObjective1.prefWidthProperty().bind(vboxCard.widthProperty());
        paneObjective2.prefHeightProperty().bind(vboxCard.heightProperty());
        paneObjective2.prefWidthProperty().bind(vboxCard.widthProperty());

        tool1.fitWidthProperty().bind(paneTool1.widthProperty());
        tool1.fitHeightProperty().bind(paneTool1.heightProperty());
        tool2.fitWidthProperty().bind(paneTool2.widthProperty());
        tool2.fitHeightProperty().bind(paneTool2.heightProperty());

        objective1.fitWidthProperty().bind(paneObjective1.widthProperty());
        objective1.fitHeightProperty().bind(paneObjective1.heightProperty());
        objective2.fitWidthProperty().bind(paneObjective2.widthProperty());
        objective2.fitHeightProperty().bind(paneObjective2.heightProperty());
    }

    private void initializeRestrictionPlayerBoard() {

        /*
        Da fare la parte di scelta della playerBoard e comunicazione con il server
        Utile: modelView.getBoard(modelView.getPlayer(localID))
        In questo caso provo a caricare senza passare dal server
        */

        BoardList boardList = new BoardList();

        Restriction restriction;
        ColorRestriction colorRestriction;
        NumberRestriction numberRestriction;
        playerBoard = new PlayerBoard(boardList.getCouple()[0]);    //0 for the first PlayerBoard, 1 for the second one
        int i, j;
        ObservableList<Node> childrens = gridPane.getChildren();
        ObservableList<Node> child;
        ImageView image;
        Pane pane;

        for (Node node : childrens) {
            node.setStyle("-fx-background-color: white");
        }

        //Insert ColorRestriction and NumberRestriction
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 5; j++) {
                insertRestriction(i,j);
            }
        }
        initializeDicePlayerBoard();
    }


    private void initializeDicePlayerBoard() {

        //Prende la playerboard del player dal server e la stampa a video; in questo caso provo il funzionamento inserendo io i dadi

        Die die;

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.SIX);
        insertDie(3, 3, die);

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.THREE);
        insertDie(2, 1, die);

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.ONE);
        insertDie(3, 1, die);

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.TWO);
        insertDie(0, 0, die);

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.TWO);
        insertDie(0, 3, die);
        insertDie(0, 4, die);

    }

    private void insertDie(int row, int column, Die die) {

        ObservableList<Node> childrens = gridPane.getChildren();
        ObservableList<Node> child;
        ImageView image;
        Pane pane;
        int i, j;
        String nameColor, character;
        int num;

        nameColor = die.getColor().toString();
        character = nameColor.substring(0, 1);
        num = die.getNumber().getInt();
        i = row;
        j = column;
        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                pane = (Pane) node;
                pane.setOpacity(1);
                child = pane.getChildren();
                image = (ImageView) child.get(0);
                ;
                image.setImage(new Image("utilsGUI/" + character + "" + num + ".png"));
                image.setVisible(true);
                image.setOpacity(1);
                break;
            }
        }
    }

    private void removeDie(int row, int column) {

        ObservableList<Node> childrens = gridPane.getChildren();
        ObservableList<Node> child;
        ImageView image;
        Pane pane;
        int i, j;

        i = row;
        j = column;
        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                pane = (Pane) node;
                child = pane.getChildren();
                image = (ImageView) child.get(0);
                image.setImage(null);
                image.setVisible(false);
                //image.setOpacity(0.3);
                break;
            }
        }
        insertRestriction(i, j);
    }

    private void insertRestriction(int row, int column) {

        Restriction restriction;
        ColorRestriction colorRestriction;
        NumberRestriction numberRestriction;
        int i, j;
        ObservableList<Node> childrens = gridPane.getChildren();
        ObservableList<Node> child;
        ImageView image;
        Pane pane;
        String nameColor;
        int num;

        i = row;
        j = column;
        restriction = playerBoard.getRestriction(i, j);
        //ColorRestriction
        if (restriction.isColorRestriction()) {
            colorRestriction = (ColorRestriction) restriction;
            for (Node node : childrens) {
                if (gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                    nameColor = colorRestriction.getColor().toString();
                    nameColor = nameColor.toLowerCase();
                    node.setStyle("-fx-background-color:"+nameColor);
                    node.setOpacity(0.3);
                    break;
                }
            }
        }
        //NumberRestriction
        else if (restriction.isNumberRestriction()) {
            numberRestriction = (NumberRestriction) restriction;
            for (Node node : childrens) {
                if (gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                    num =  numberRestriction.getNumber().getInt();
                    pane = (Pane) node;
                    child = pane.getChildren();
                    image = (ImageView) child.get(0);
                    image.setImage(new Image("utilsGUI/numberRestriction"+num+".png"));
                    image.setVisible(true);
                    image.setOpacity(0.3);
                    break;
                }
            }
        }
        //NoRestriction
        else {
            for (Node node : childrens) {
                if (gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                    node.setStyle("-fx-background-color: white");
                    node.setOpacity(1);
                    break;
                }
            }
        }
    }


    private void initializePrivateObjective() {
        //Da aggiungere richiesta al server
        privateObjective.setText("Rosso");
        privateObjective.setStyle("-fx-text-fill:red");
    }

    public void handlezoomTool1(ActionEvent event) {

        Stage popupStage= new Stage();
        ImageView zoomTool1;
        VBox layout = new VBox(10);
        Scene scene1 = new Scene(layout, 350, 500);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Tool1");

        //Richiesta server tool1
        zoomTool1 = new ImageView();
        zoomTool1.setImage(new Image("toolCard/toolCard01.jpeg"));
        zoomTool1.fitHeightProperty().bind(scene1.heightProperty());
        zoomTool1.fitWidthProperty().bind(scene1.widthProperty());
        zoomTool1.setPreserveRatio(true);

        layout.getChildren().addAll( zoomTool1);
        layout.setAlignment(Pos.CENTER);
        popupStage.setScene(scene1);
        popupStage.setMinWidth(200);
        popupStage.setMinHeight(300);
        popupStage.showAndWait();

        /*
        Button close = new Button("Close this pop up window");
        close.setOnAction(e -> popupStage.close());
        */
    }
    public void handlezoomTool2(ActionEvent event) {

        Stage popupStage= new Stage();
        ImageView zoomTool1;
        VBox layout = new VBox(10);
        Scene scene1 = new Scene(layout, 350, 500);

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Tool1");

        //Richiesta server tool1
        zoomTool1 = new ImageView();
        zoomTool1.setImage(new Image("toolCard/toolCard02.jpeg"));
        zoomTool1.fitHeightProperty().bind(scene1.heightProperty());
        zoomTool1.fitWidthProperty().bind(scene1.widthProperty());
        zoomTool1.setPreserveRatio(true);

        layout.getChildren().addAll( zoomTool1);
        layout.setAlignment(Pos.CENTER);
        popupStage.setScene(scene1);
        popupStage.setMinWidth(200);
        popupStage.setMinHeight(300);
        popupStage.showAndWait();

        /*
        Button close = new Button("Close this pop up window");
        close.setOnAction(e -> popupStage.close());
        */
    }

    public void handleRiserva(ActionEvent event) {

        Stage popupStage= new Stage();
        ImageView[] imageRiserva = new ImageView[9];
        VBox layoutVbox = new VBox(20);
        HBox layoutHbox = new HBox(10);
        Label label;
        Scene scene1 = new Scene(layoutVbox, 1000, 200);
        int i;

        //popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initModality(Modality.NONE);
        popupStage.setTitle("Riserva");

        label = new Label("Riserva:");
        label.setFont(Font.font("System", 16));
        label.setStyle("-fx-font-weight: bold");

        //Richiesta server per ogni dado della riserva; sistemare ciclo for in base alla lunghezza della riserva (DraftPool.size());

        for (i = 0; i < 9; i++){
            imageRiserva[i] = new ImageView();
            imageRiserva[i].setImage(new Image("utilsGUI/B1.png"));
            imageRiserva[i].setFitHeight(100);
            imageRiserva[i].setFitWidth(100);
            imageRiserva[i].setPreserveRatio(true);
            layoutHbox.getChildren().add(imageRiserva[i]);
        }

        layoutHbox.setAlignment(Pos.CENTER);
        layoutVbox.getChildren().addAll(label, layoutHbox);
        popupStage.setScene(scene1);
        popupStage.setMinWidth(1000);
        popupStage.setMinHeight(200);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        popupStage.setX((primScreenBounds.getWidth() - popupStage.getWidth()));
        popupStage.setY((primScreenBounds.getHeight() - 300));
        popupStage.showAndWait();
    }

    public void handleTracciato(ActionEvent event) {

        //Richiesta server per ogni dado del tracciato

        Stage popupStage= new Stage();
        ImageView[] imageRound = new ImageView[9];
        VBox layoutVbox = new VBox(20);
        HBox tracciatoHbox= new HBox(10);
        VBox columnRoundVbox[] = new VBox[10];
        Label label;
        Scene scene1 = new Scene(layoutVbox, 900, 800);
        int i, j;

        popupStage.initModality(Modality.NONE);
        popupStage.setTitle("Tracciato");

        label = new Label("Tracciato:");
        label.setFont(Font.font("System", 16));
        label.setStyle("-fx-font-weight: bold");

        for (i = 0; i < 10; i++){
            columnRoundVbox[i] = new VBox(10);
        }

        //Sistemare ciclo in base ai dati che arrivano dal server.. imageRound viene inizializzato con una lista del round i-esimo

        for (i = 0; i < 10; i++){   //Round

            //imageRound viene inizializzato con una lista del round i-esimo  ... da fare

            for (j = 0; j < 9; j++) {   //Dadi nel round
                imageRound[j] = new ImageView();
                imageRound[j].setImage(new Image("utilsGUI/B1.png"));
                imageRound[j].setFitHeight(70);
                imageRound[j].setFitWidth(70);
                imageRound[j].setPreserveRatio(true);
                columnRoundVbox[i].getChildren().add(imageRound[j]);
            }

            tracciatoHbox.getChildren().add(columnRoundVbox[i]);
        }

        tracciatoHbox.setAlignment(Pos.CENTER);
        layoutVbox.getChildren().addAll(label, tracciatoHbox);
        popupStage.setScene(scene1);
        popupStage.setMinWidth(900);
        popupStage.setMinHeight(800);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        popupStage.setX((primScreenBounds.getWidth())/2);
        //popupStage.setY((primScreenBounds.getHeight() - 300));
        popupStage.showAndWait();
    }
}
