package it.polimi.se2018.view;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ChooseRoundTrackDie extends JDialog implements MouseListener{
    private JPanel contentPane;
    private JPanel board;
    public final CountDownLatch latch = new CountDownLatch(1);
    private Map<Color,String> colorMap = new HashMap<>();
    private int[] p = new int[2];
    private RoundTrack roundTrack;

    public ChooseRoundTrackDie(RoundTrack roundTrack) {
        p[0] = 0;
        p[1] = 0;

        this.roundTrack = roundTrack;

        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

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

        for(int i = 0; i < 9; i ++) {
            for (int j = 0; j < 10; j++) {
                ((JLabel) (((JPanel) board.getComponent(i + 9 * j)).getComponent(0))).addMouseListener(this);
            }
        }
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 10; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 9 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
            }
        }
    }

    private void populateBoard(RoundTrack roundTrack){
        whiteRefreshBoard();

        for(int i = 0; i < 10; i ++) {
            for (int j = 0; j < roundTrack.numberOfDice(i); j++) {
                try {
                    ((JLabel) (((JPanel) board.getComponent(i + 10 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(roundTrack.getDie(i, j).getColor()) + "" + roundTrack.getDie(i, j).getNumber().getInt() + ".png"));
                } catch (Exception e) {}
            }
        }
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    public int[] getPosition(){
        this.pack();
        this.setVisible(true);
        try {
            latch.await();
        }catch (InterruptedException e){}
        return p;
    }

    private void onOK() {
        latch.countDown();
    }

    private void onCancel() {
        latch.countDown();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        for(int i = 0; i < 10; i ++) {
            for (int j = 0; j < roundTrack.numberOfDice(i); j++) {
                if (((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).equals(source)) {
                    p[0] = i;
                    p[1] = j;
                    latch.countDown();
                    return;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
