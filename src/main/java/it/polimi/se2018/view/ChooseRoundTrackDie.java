package it.polimi.se2018.view;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ChooseRoundTrackDie extends JDialog implements MouseListener{
    private JPanel contentPane;
    private JPanel board;
    private final CountDownLatch latch = new CountDownLatch(1);
    private int[] p = new int[2];
    private RoundTrack roundTrack;

    protected ChooseRoundTrackDie(RoundTrack roundTrack) {
        p[0] = 0;
        p[1] = 0;

        this.roundTrack = roundTrack;

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

        populateBoard(roundTrack);

        for(int i = 0; i < 10; i ++) {
            for (int j = 0; j < 9; j++) {
                ((JLabel) (((JPanel) board.getComponent(i + 10 * j)).getComponent(0))).addMouseListener(this);
            }
        }
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 10; i ++){
            for(int j = 0; j < 9; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 10 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
            }
        }
    }

    private void populateBoard(RoundTrack roundTrack){
        whiteRefreshBoard();

        for(int i = 0; i < 10; i ++) {
            for (int j = 0; j < roundTrack.numberOfDice(i); j++) {
                try {
                    ((JLabel) (((JPanel) board.getComponent(i + 10 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(roundTrack.getDie(i, j).getColor()) + "" + roundTrack.getDie(i, j).getNumber().getInt() + ".png"));
                } catch (Exception e) {}
            }
        }
    }

    public int[] getPosition(){
        this.pack();
        this.setVisible(true);
        try {
            latch.await();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return p;
    }

    private void onCancel() {
        latch.countDown();
        dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        for(int i = 0; i < 10; i ++) {
            for (int j = 0; j < 9; j++) {
                if (((JLabel) (((JPanel) board.getComponent(i + 10 * j)).getComponent(0))).equals(source)) {
                    p[0] = i;
                    p[1] = j;
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

    public static void main(String[] args) {
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDie(0,new Die(Color.RED));
        ChooseRoundTrackDie dialog = new ChooseRoundTrackDie(roundTrack);
        int[] pos = dialog.getPosition();
        System.out.println(pos[0] + " " + pos[1]);
    }

}
