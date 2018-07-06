package it.polimi.se2018.view;

import it.polimi.se2018.utils.messages.ServerMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EndGame extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JPanel board;
    private JLabel player1;
    private JLabel player2;
    private JLabel player3;
    private JLabel player4;
    private JLabel winnerLabel;
    private ServerMessage serverMessage;

    protected EndGame(ServerMessage serverMessage) {

        this.serverMessage = serverMessage;

        setContentPane(contentPane);
        setModal(true);

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

        end();

    }

    private void end() {
        int top = 0;
        String[] nicknames = new String[serverMessage.getScores().size()];
        nicknames = serverMessage.getScores().keySet().toArray(nicknames);
        ArrayList<JLabel> playerLabels = new ArrayList<>();
        playerLabels.add(player1);
        playerLabels.add(player2);
        playerLabels.add(player3);
        playerLabels.add(player4);

        for(int i = 0; i < serverMessage.getScores().size(); i ++){
            if (serverMessage.getScores().get(nicknames[i]) > top) {
                top = serverMessage.getScores().get(nicknames[i]);
            };
            playerLabels.get(i).setText(nicknames[i] + ": " + serverMessage.getScores().get(nicknames[i]));
        }

        for (String nick : serverMessage.getScores().keySet()) {
            if (serverMessage.getScores().get(nick) == top) {
                winnerLabel.setText("Il giocatore " + nick + " ha vinto");
            }

        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Map<String,Integer> prova = new HashMap<>();
        prova.put("Matteo",10);
        prova.put("Alessio",20);
        prova.put("Giamp",5);
        prova.put("SanPietro",60);
        ServerMessage serverMessage = new ServerMessage(prova);
        EndGame dialog = new EndGame(serverMessage);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}