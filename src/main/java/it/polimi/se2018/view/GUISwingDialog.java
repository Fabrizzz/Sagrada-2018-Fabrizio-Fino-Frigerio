package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.messages.ClientMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUISwingDialog extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField indirizzo;
    private JTextField porta;
    private JRadioButton rmi;
    private JRadioButton socketRadioButton;
    private JTextField nickname;
    private ClientNetwork clientNetwork;
    private GUISwingProxy guiSwingProxy;
    private Long localID;

    public GUISwingDialog() {
        guiSwingProxy = new GUISwingProxy();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        ButtonGroup group = new ButtonGroup();
        group.add(rmi);
        group.add(socketRadioButton);
        socketRadioButton.setSelected(true);
        clientNetwork = new ClientNetwork(guiSwingProxy);
    }

    private void onOK() {
        String address = indirizzo.getText();
        int port = 0;
        if(socketRadioButton.isSelected()){
            port = Integer.parseInt(porta.getText());
            if(Server.available(port) || !clientNetwork.connectSocket(address, port)){
                JOptionPane.showMessageDialog(this, "Errore connessione.");
                return;
            }
        }else{
            if(!clientNetwork.connectRMI(address)){
                JOptionPane.showMessageDialog(this, "Errore connessione.");
                return;
            }
        }


        String nick = nickname.getText();
        localID = JSONUtils.readID(nick);
        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            JOptionPane.showMessageDialog(this, "Nome utente inviato, attendi inizio partita");
            dispose();
            GUISwing game = new GUISwing(guiSwingProxy);
            guiSwingProxy.gameWindow(game);
            game.pack();
            game.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "Errore connessione.");
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GUISwingDialog dialog = new GUISwingDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
