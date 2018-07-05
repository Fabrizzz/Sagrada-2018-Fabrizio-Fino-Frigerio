package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;

public class GUISwing extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel board;
    private Map<Color,String> colorMap = new HashMap<>();
    private ModelView modelView;
    private Long localID;
    private GUISwingProxy guiSwingProxy;

    public GUISwing(GUISwingProxy guiSwingProxy) {
        this.guiSwingProxy = guiSwingProxy;
        localID = new Long(123);
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        whiteRefreshBoard();
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png",false));
            }
        }
    }

    public void setModelView(ModelView modelView){
        this.modelView = modelView;
        refreshBoard();
    }

    public void chooseBoard(Board[] boards){
        GUISwingChooseBoard dialog = new GUISwingChooseBoard(boards,this);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void selectedBoard(Board board){
        guiSwingProxy.selectedBoard(board);
    }

    public ModelView getModelView(){
        return modelView;
    }

    public void endGame(Map<String,Integer> scores){
        //todo
    }

    public void printError(String error){
        JOptionPane.showMessageDialog(this, error);
    }
    private void refreshBoard(){
        whiteRefreshBoard();

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(((ColorRestriction) modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j)).getColor()) +".png",false));
                }else if(modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j)).getNumber().getInt() +".png",false));
                }
            }
        }

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(modelView.getBoard(modelView.getPlayer(localID)).containsDie(i,j)){
                    try{
                        ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(modelView.getBoard(modelView.getPlayer(localID)).getDie(i,j).getColor()) + modelView.getBoard(modelView.getPlayer(localID)).getDie(i,j).getNumber().getInt() +".png",false));
                    }catch (Exception e){}
                }
            }
        }
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }
    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
