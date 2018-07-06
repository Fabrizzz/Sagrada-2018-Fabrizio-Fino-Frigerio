package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

public class GenericRadio extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JButton confermaButton;
    private JLabel domandaLabel;
    private final CountDownLatch latch = new CountDownLatch(1);

    public GenericRadio(String question) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        domandaLabel.setText(question);

        confermaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(siRadioButton);
        group.add(noRadioButton);
        siRadioButton.setSelected(true);
    }

    public boolean getValue(){

        this.pack();
        this.setVisible(true);

        try{
            latch.await();
        }catch (InterruptedException e){}

        return siRadioButton.isSelected();
    }

    private void onOK() {
        latch.countDown();
    }

    private void onCancel() {
        latch.countDown();
    }

    public static void main(String[] args) {
        GenericRadio dialog = new GenericRadio("Vuoi spostare un altro dado?");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
