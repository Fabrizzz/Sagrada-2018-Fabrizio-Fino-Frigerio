package it.polimi.se2018.view;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ChooseCell extends JDialog implements MouseListener {
    private JPanel contentPane;
    private JPanel board;
    private JLabel messaggeLabel;
    private PlayerBoard playerBoard;
    private int r,c;
    public final CountDownLatch latch = new CountDownLatch(1);

    public ChooseCell(PlayerBoard playerBoard,String message) {
        r = 0;
        c = 0;

        this.playerBoard = playerBoard;
        setContentPane(contentPane);
        setModal(true);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        showBoard();
        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).addMouseListener(this);
            }
        }

        messaggeLabel.setText(message);
    }

    public int[] getPosition(){
        this.pack();
        this.setVisible(true);

        try{
            latch.await();
        }catch (InterruptedException e){}
        int[] pos = new int[2];
        pos[0] = r;
        pos[1] = c;
        return pos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).equals(source)){
                    r = i;
                    c = j;
                    latch.countDown();
                    onCancel();
                    return;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}


    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
            }
        }
    }

    private void showBoard(){
        whiteRefreshBoard();

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(((ColorRestriction) playerBoard.getRestriction(i,j)).getColor()) +".png"));
                }else if(playerBoard.getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) playerBoard.getRestriction(i,j)).getNumber().getInt() +".png"));
                }
            }
        }

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.containsDie(i,j)){
                    try{
                        ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(playerBoard.getDie(i,j).getColor()) + playerBoard.getDie(i,j).getNumber().getInt() +".png"));
                    }catch (Exception e){}
                }
            }
        }


    }

    private void onOK() {
        latch.countDown();
        dispose();
    }

    private void onCancel() {
        latch.countDown();
        dispose();
    }
}
