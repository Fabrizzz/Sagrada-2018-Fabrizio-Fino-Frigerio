package it.polimi.se2018.view;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GUISwingRoundTrack extends JDialog {
    private JPanel contentPane;
    private JPanel board;
    private Map<Color,String> colorMap = new HashMap<>();

    public GUISwingRoundTrack(RoundTrack roundTrack) {
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

        setContentPane(contentPane);
        setModal(false);

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
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 10; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 9 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
                //((JLabel) (((JPanel) board.getComponent(i + 9 * j)).getComponent(0))).setText("" + (i + 9 * j));
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

    private void onCancel() {
        dispose();
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }


    public static void main(String[] args) {
        RoundTrack roundTrack = new RoundTrack();
        roundTrack.addDie(0,new Die(Color.RED));
        roundTrack.addDie(0,new Die(Color.RED));
        roundTrack.addDie(0,new Die(Color.BLUE));
        roundTrack.addDie(0,new Die(Color.YELLOW));
        roundTrack.addDie(2,new Die(Color.PURPLE));
        roundTrack.addDie(3,new Die(Color.GREEN));
        roundTrack.addDie(4,new Die(Color.RED));

        GUISwingRoundTrack dialog = new GUISwingRoundTrack(roundTrack);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
