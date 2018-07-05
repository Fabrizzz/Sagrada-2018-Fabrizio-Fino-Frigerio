package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;

public class GUISwing {
    private JPanel contentPane;
    private JPanel board;
    private JButton mostraRiservaButton;
    private JButton mostraTracciatoDeiDadiButton;
    private JLabel tool1Label;
    private JLabel tool2Label;
    private JLabel tool3Label;
    private Map<Color,String> colorMap = new HashMap<>();
    private Map<Tool,String> toolCardMap = new HashMap<>();
    private ModelView modelView;
    private Long localID;
    private GUISwingProxy guiSwingProxy;

    public GUISwing(GUISwingProxy guiSwingProxy) {
        this.guiSwingProxy = guiSwingProxy;;
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");

        toolCardMap.put(Tool.PINZASGROSSATRICE,"toolCard01");
        toolCardMap.put(Tool.PENNELLOPEREGLOMISE,"toolCard02");
        toolCardMap.put(Tool.ALESATOREPERLAMINADIRAME,"toolCard03");
        toolCardMap.put(Tool.LATHEKIN,"toolCard04");
        toolCardMap.put(Tool.TAGLIERINACIRCOLARE,"toolCard05");
        toolCardMap.put(Tool.PENNELLOPERPASTASALDA,"toolCard06");
        toolCardMap.put(Tool.MARTELLETTO,"toolCard07");
        toolCardMap.put(Tool.TENAGLIAAROTELLE,"toolCard08");
        toolCardMap.put(Tool.RIGAINSUGHERO,"toolCard09");
        toolCardMap.put(Tool.TAMPONEDIAMANTATO,"toolCard10");
        toolCardMap.put(Tool.DILUENTEPERPASTASALDA,"toolCard11");
        toolCardMap.put(Tool.TAGLIERINAMANUALE,"toolCard12");

        //setContentPane(contentPane);
        //setModal(true);

        mostraRiservaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraRiserva();
            }
        });

        mostraTracciatoDeiDadiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraTracciatoDadi();
            }
        });


        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        whiteRefreshBoard();
    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png",false));
            }
        }
    }

    public void setModelView(ModelView modelView){
        this.modelView = modelView;
        refreshBoard();
    }

    public void setId(Long localID){
        this.localID = localID;
    }

    public void chooseBoard(Board[] boards){
        GUISwingChooseBoard dialog = new GUISwingChooseBoard(boards,this);
        dialog.pack();
        dialog.setVisible(true);
    }

    public void selectedBoard(Board board){
        guiSwingProxy.selectedBoard(board);
    }

    public ModelView getModelView(){
        return modelView;
    }

    public void endGame(Map<String,Integer> scores){
        //todo
    }

    public void printError(String error){
        JOptionPane.showMessageDialog(this.contentPane, error);
    }

    public void mostraTracciatoDadi(){
        if(modelView != null) {
            GUISwingRoundTrack dialog = new GUISwingRoundTrack(modelView.getRoundTrack());
            dialog.pack();
            dialog.setVisible(true);
        }else{
            GUISwingRoundTrack dialog = new GUISwingRoundTrack(new RoundTrack());
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void toolCardRefresh(){
        Tool[] tools = modelView.getTools().keySet().toArray(new Tool[modelView.getTools().keySet().size()]);

        tool1Label.setIcon(new StretchIcon("src/main/resources/toolCard/" + toolCardMap.get(tools[0]) + ".png"));
        tool2Label.setIcon(new StretchIcon("src/main/resources/toolCard/" + toolCardMap.get(tools[1]) + ".png"));
        tool3Label.setIcon(new StretchIcon("src/main/resources/toolCard/" + toolCardMap.get(tools[2]) + ".png"));
    }

    private void refreshBoard(){
        whiteRefreshBoard();
        toolCardRefresh();
        try{
            modelView.getBoard(modelView.getPlayer(localID)).getRestriction(0,0).isColorRestriction();
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(((ColorRestriction) modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j)).getColor()) +".png",false));
                }else if(modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) modelView.getBoard(modelView.getPlayer(localID)).getRestriction(i,j)).getNumber().getInt() +".png",false));
                }
            }
        }

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(modelView.getBoard(modelView.getPlayer(localID)).containsDie(i,j)){
                    try{
                        ((JLabel) (((JPanel) board.getComponent(i + 4 * j)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(modelView.getBoard(modelView.getPlayer(localID)).getDie(i,j).getColor()) + modelView.getBoard(modelView.getPlayer(localID)).getDie(i,j).getNumber().getInt() +".png",false));
                    }catch (Exception e){}
                }
            }
        }
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }


    public void mostraRiserva(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUISwingRiserva dialog = new GUISwingRiserva(modelView.getDraftPool());
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
    }

    public JPanel getContentPane(){
        return contentPane;
    }
    public static void main(String[] args) {
        Model model = ModelControllerInitializerTest.initialize(Tool.LATHEKIN);
        model.getRoundTrack().addDie(0,new Die(Color.RED));
        model.getRoundTrack().addDie(0,new Die(Color.BLUE));
        model.getRoundTrack().addDie(0,new Die(Color.YELLOW));
        model.getDraftPool().addDie(new Die(Color.RED));
        model.getDraftPool().addDie(new Die(Color.BLUE));

        ModelView modelView = new ModelView(model);

        GUISwingProxy guiSwingProxy = new GUISwingProxy();
        JFrame frame = new JFrame("Sagrada");
        GUISwing game = new GUISwing(guiSwingProxy);
        game.setId((long) 123);
        game.setModelView(modelView);
        frame.setContentPane(game.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}
