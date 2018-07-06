package it.polimi.se2018.view;

import it.polimi.se2018.model.DraftPool;

import javax.swing.*;
import java.awt.event.*;

public class ShowDraftPool extends JDialog {
    private JPanel contentPane;
    private JPanel board;

    protected ShowDraftPool(DraftPool draftPool) {
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
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(draftPool.getDie(i).getColor()) + "" + draftPool.getDie(i).getNumber().getInt() + ".png"));
            } catch (Exception e) {}
        }

    }

    private void onCancel() {
        dispose();
    }
}
