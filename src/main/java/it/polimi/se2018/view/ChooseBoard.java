package it.polimi.se2018.view;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alessio
 */
public class ChooseBoard extends JDialog {
    private JPanel contentPane;
    private JPanel board;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JButton choose;
    private JLabel boardName;
    private JLabel difficultyLabel;
    private GUIMain guiMain;
    private Board[] boards;

    /**
     * Costructor
     * @param boards
     * @param guiMain
     */
    protected ChooseBoard(Board[] boards, GUIMain guiMain) {

        this.guiMain = guiMain;
        this.boards = boards;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(choose);

        choose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        group.add(radioButton3);
        group.add(radioButton4);
        radioButton1.setSelected(true);

        radioButton1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                refreshBoard(boards[0]);
            }
        });

        radioButton2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                refreshBoard(boards[1]);
            }
        });

        radioButton3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                refreshBoard(boards[2]);
            }
        });

        radioButton4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                refreshBoard(boards[3]);
            }
        });

        refreshBoard(boards[0]);
    }

    /**
     * clean the Board
     */
    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
            }
        }
    }

    /**
     * Insert the dice and the constraints in the Board
     * @param boardS
     */
    private void refreshBoard(Board boardS){
        PlayerBoard playerBoard = new PlayerBoard(boardS);
        boardName.setText("Nome: " + boardS.getName());
        difficultyLabel.setText("Difficoltà: " + boardS.getTokens());
        whiteRefreshBoard();

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(((ColorRestriction) playerBoard.getRestriction(i,j)).getColor()) +".png"));
                }else if(playerBoard.getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) playerBoard.getRestriction(i,j)).getNumber().getInt() +".png"));
                }
            }
        }
        repaint();
    }

    /**
     * Manage the ok button
     */
    private void onOK() {
        if(radioButton1.isSelected()){
            guiMain.selectedBoard(boards[0]);
        }else if(radioButton2.isSelected()){
            guiMain.selectedBoard(boards[1]);
        }else if(radioButton3.isSelected()){
            guiMain.selectedBoard(boards[2]);
        }else if(radioButton4.isSelected()){
            guiMain.selectedBoard(boards[3]);
        }
        dispose();
    }
}
