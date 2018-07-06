package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;

public class ShowDie extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel dieLabel;

    public ShowDie(String imagePath) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        dieLabel.setIcon(new StretchIcon(imagePath));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ShowDie dialog = new ShowDie("src/main/resources/utilsGUI/B1.png");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
