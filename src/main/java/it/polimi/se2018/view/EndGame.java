package it.polimi.se2018.view;

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
    Map<String,Integer> map;

    public EndGame(Map<String,Integer> scores) {

        map = new HashMap<String, Integer>(scores);

        setContentPane(contentPane);
        setModal(true);

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

        end();

    }

    private void end() {

        int i;
        String nick;
        int dim = map.size();

        if (map == null){
            //Errore
        }
        else {
            nick = top();
            player1.setText(nick+": "+map.get(nick));
            player1.setForeground (Color.red);
            map.remove(nick);
            if (dim>1) {
                nick = top();
                player2.setText(nick + ": " + map.get(nick));
                map.remove(nick);
                if (dim>2) {
                    nick = top();
                    player3.setText(nick + ": " + map.get(nick));
                    map.remove(nick);
                    if(dim>3) {
                        nick = top();
                        player4.setText(nick + ": " + map.get(nick));
                    }
                }
            }
        }
    }

    private String top(){

        int top = 0;
        String temp = null;

        for(String nick : map.keySet()){
            if(map.get(nick) > top){
                top = map.get(nick);
                temp = nick;
            }
        }
        return temp;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Map<String,Integer> prova = new HashMap<>();
        prova.put("Matteo",10);
        prova.put("Alessio",20);
        prova.put("Giamp",5);
        prova.put("SanPietro",60);
        EndGame dialog = new EndGame(prova);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}