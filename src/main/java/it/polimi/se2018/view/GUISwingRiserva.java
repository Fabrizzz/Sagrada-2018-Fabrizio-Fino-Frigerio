package it.polimi.se2018.view;

import it.polimi.se2018.model.DiceBag;
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
        setModal(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

        populateBoard(draftPool);
    }

    private void whiteRefresh(){
        for(int i = 0; i < 9; i ++){
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
            }
    }

    private void populateBoard(DraftPool draftPool){
        whiteRefresh();

        for(int i = 0; i < draftPool.size(); i ++) {
            try {
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(draftPool.getDie(i).getColor()) + "" + draftPool.getDie(i).getNumber().getInt() + ".png"));
            } catch (Exception e) {}
        }

    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    private void onCancel() {
        dispose();
    }
}
