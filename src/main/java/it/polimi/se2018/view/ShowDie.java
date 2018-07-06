package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;

public class ShowDie extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel dieLabel;

    protected ShowDie(String imagePath) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        dieLabel.setIcon(new StretchIcon(imagePath));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }


    public static void main(String[] args) {
        ShowDie dialog = new ShowDie("src/main/resources/utilsGUI/B1.png");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
