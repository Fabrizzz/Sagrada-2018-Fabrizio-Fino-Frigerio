package it.polimi.se2018.view;

import it.polimi.se2018.utils.enums.NumberEnum;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

public class ChooseNewValue extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox comboBox1;
    private final CountDownLatch latch = new CountDownLatch(1);

    protected ChooseNewValue() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        comboBox1.setSelectedIndex(0);
    }

    protected NumberEnum getNewValue(){
        this.pack();
        this.setVisible(true);
        try{
            latch.await();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return NumberEnum.getNumber(comboBox1.getSelectedIndex() + 1);
    }

    private void onOK() {
        latch.countDown();
        dispose();
    }

    public static void main(String[] args) {
        ChooseNewValue dialog = new ChooseNewValue();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
