package it.polimi.se2018.view;

import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GUISwingRiserva extends JDialog {
    private JPanel contentPane;
    private JPanel board;
    private Map<Color,String> colorMap = new HashMap<>();

    public GUISwingRiserva(DraftPool draftPool) {
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");
        setContentPane(contentPane);
        setModal(true);

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
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 9; i ++){
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png",false));
                //((JLabel) (((JPanel) board.getComponent(i + 9 * j)).getComponent(0))).setText("" + (i + 9 * j));
            }
    }

    private void populateBoard(DraftPool draftPool){
        whiteRefreshBoard();

        for(int i = 0; i < draftPool.size(); i ++) {
            try {
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(draftPool.getDie(i).getColor()) + "" + draftPool.getDie(i).getNumber().getInt() + ".png", false));
            } catch (Exception e) {}
        }

    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*public static void main(String[] args) {
        GUISwingRiserva dialog = new GUISwingRiserva();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }*/
}
