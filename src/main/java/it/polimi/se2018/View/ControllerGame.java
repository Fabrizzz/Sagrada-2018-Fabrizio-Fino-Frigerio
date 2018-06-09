package it.polimi.se2018.View;

import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Restriction;
import it.polimi.se2018.utils.enums.Color;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGame implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    BorderPane borderPane;

    @FXML
    GridPane gridPane;

    @FXML
    Region regionLeft;

    @FXML
    FlowPane flowPane;

    @FXML
    HBox hbox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLayout();
        initializePlayerBoard();

    }

    private void initializeLayout(){

        gridPane.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");
        borderPane.setStyle("-fx-border-color: black");
        regionLeft.setStyle("-fx-border-color: black");
        flowPane.setStyle("-fx-border-color: black");
        hbox.setStyle("-fx-border-color: black");

        //BorderPane resizable
        borderPane.prefWidthProperty().bind(root.widthProperty());
        borderPane.prefHeightProperty().bind(root.heightProperty());

    }

    private void initializePlayerBoard(){

        /*
        Da fare la parte di scelta della playerBoard e comunicazione con il server
        Utile: modelView.getBoard(modelView.getPlayer(localID))
        Caricamento di una playerBoard che in questo caso è la KALEIDOSCOPICDREAM ma le istruzioni sono generali per qualsiasi altra
        */

        Restriction restriction;
        ColorRestriction colorRestriction;
        int i,j;
        PlayerBoard playerBoard = new PlayerBoard(BoardList.getCouple()[0]);
        ObservableList<Node> childrens = gridPane.getChildren();

        //ColorRestriction
        for (i = 0; i < 4; i++){
            for(j = 0; j < 5; j++){
                restriction = playerBoard.getRestriction(i,j);
                if (restriction.isColorRestriction()){
                    colorRestriction = (ColorRestriction) restriction;
                    for (Node node : childrens) {
                        if(gridPane.getRowIndex(node) == i && gridPane.getColumnIndex(node) == j) {
                            if(colorRestriction.getColor() == Color.BLUE){
                                node.setStyle("-fx-background-color: blue");
                            }
                            else if(colorRestriction.getColor() == Color.RED){
                                node.setStyle("-fx-background-color: red");
                            }
                            else if(colorRestriction.getColor() == Color.YELLOW){
                                node.setStyle("-fx-background-color: yellow");
                            }
                            else if(colorRestriction.getColor() == Color.PURPLE){
                                node.setStyle("-fx-background-color: purple");
                            }
                            else if(colorRestriction.getColor() == Color.GREEN){
                                node.setStyle("-fx-background-color: green");
                            }
                            break;
                        }
                    }
                }
            }
        }



    }
}
