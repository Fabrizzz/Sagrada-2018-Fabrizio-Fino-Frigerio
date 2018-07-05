package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

public class GUIAumentaValoreDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JRadioButton aumentaDiUnoRadioButton;
    private JRadioButton diminuisciDiUnoRadioButton;
    private final CountDownLatch latch = new CountDownLatch(1);

    public GUIAumentaValoreDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(aumentaDiUnoRadioButton);
        group.add(diminuisciDiUnoRadioButton);
        aumentaDiUnoRadioButton.setSelected(true);
    }

    public boolean getValue(){
        this.pack();
        this.setVisible(true);
        try{
            latch.await();
        }catch (InterruptedException e){}

        return aumentaDiUnoRadioButton.isSelected();
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
        GUIAumentaValoreDialog dialog = new GUIAumentaValoreDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
