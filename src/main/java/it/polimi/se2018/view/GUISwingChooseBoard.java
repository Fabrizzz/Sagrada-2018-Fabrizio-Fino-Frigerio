package it.polimi.se2018.view;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUISwingChooseBoard extends JDialog {
    private JPanel contentPane;
    private JPanel board;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JButton choose;
    private JLabel boardName;
    private Map<Color,String> colorMap = new HashMap<>();
    private GUISwing guiSwing;
    private Board[] boards;

    public GUISwingChooseBoard(Board[] boards,GUISwing guiSwing) {
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

        this.guiSwing = guiSwing;
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

    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png",false));
            }
        }
    }

    private void refreshBoard(Board boardS){
        PlayerBoard playerBoard = new PlayerBoard(boardS);
        boardName.setText("Nome: " + boardS.getName());
        whiteRefreshBoard();

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(((ColorRestriction) playerBoard.getRestriction(i,j)).getColor()) +".png",false));
                }else if(playerBoard.getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) playerBoard.getRestriction(i,j)).getNumber().getInt() +".png",false));
                }
            }
        }
        repaint();
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    private void onOK() {
        if(radioButton1.isSelected()){
            guiSwing.selectedBoard(boards[0]);
        }else if(radioButton2.isSelected()){
            guiSwing.selectedBoard(boards[1]);
        }else if(radioButton3.isSelected()){
            guiSwing.selectedBoard(boards[2]);
        }else if(radioButton4.isSelected()){
            guiSwing.selectedBoard(boards[3]);
        }
        dispose();
    }

    /*public static void main(String[] args) {
        Board[] boardsss = new Board[4];
        BoardList boardList = new BoardList();
        ArrayList<Board> boards = new ArrayList<>();
        boards.add(boardList.getCouple()[0]);
        boards.add(boardList.getCouple()[0]);
        boards.add(boardList.getCouple()[0]);
        boards.add(boardList.getCouple()[0]);
        GUISwingChooseBoard dialog = new GUISwingChooseBoard(boards.toArray(boardsss));
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}
