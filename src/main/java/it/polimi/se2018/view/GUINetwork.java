package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.messages.ClientMessage;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author Alessio
 */
public class GUINetwork extends JDialog{
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
    private GUIMain game;

    /**
     * Costructor
     */
    public GUINetwork() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        guiSwingProxy = new GUISwingProxy();

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

        ButtonGroup group = new ButtonGroup();
        group.add(rmi);
        group.add(socketRadioButton);
        socketRadioButton.setSelected(true);
    }

    /**
     * manage the ok button and create the connection
     */
    private void onOK() {
        String address = indirizzo.getText();
        int port = 0;
        String nick = nickname.getText();
        localID = JSONUtils.readID(nick);
        game = new GUIMain(guiSwingProxy);
        game.setId(localID);
        guiSwingProxy.gameWindow(game,localID);
        clientNetwork = new ClientNetwork(guiSwingProxy);

        if(socketRadioButton.isSelected()){
            try{
                port = Integer.parseInt(porta.getText());
            }catch (Exception e){
                port = 0;
            }
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

        ClientMessage clientMessage = new ClientMessage(nick,localID);

        if(clientNetwork.sendMessage(clientMessage)){
            JOptionPane.showMessageDialog(this, "Nome utente inviato, attendi inizio partita");
            dispose();

            JFrame frame = new JFrame("Sagrada");
            frame.setContentPane(game.getContentPane());
            frame.setSize(500,400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "Errore connessione.");
        }

    }

    /**
     * manage cancel button
     */
    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        GUINetwork dialog = new GUINetwork();
        dialog.pack();
        dialog.setVisible(true);
    }
}
