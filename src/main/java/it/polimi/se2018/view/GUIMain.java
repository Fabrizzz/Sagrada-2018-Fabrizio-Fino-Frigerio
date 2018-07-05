package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.PlayerMove;

public class GUIMain {
    private JPanel contentPane;
    private JPanel board;
    private JButton mostraRiservaButton;
    private JButton mostraTracciatoDeiDadiButton;
    private JLabel tool1Label;
    private JLabel tool2Label;
    private JLabel tool3Label;
    private JButton piazzaUnDadoButton;
    private JComboBox comboBoxPlayerBoard;
    private JLabel labelTurn;
    private JLabel labelSegnalini;
    private JLabel labelRound;
    private JButton terminaTurnoButton;
    private JButton mostraLeCarteObiettivoButton;
    private Map<Color,String> colorMap = new HashMap<>();
    private Map<Tool,String> toolCardMap = new HashMap<>();
    private ModelView modelView;
    private Long localID;
    private GUISwingProxy guiSwingProxy;

    public GUIMain(GUISwingProxy guiSwingProxy) {
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

        mostraLeCarteObiettivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraObiettivi();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        whiteRefreshBoard();
        piazzaUnDadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                normalSugheroMove();
            }
        });
        terminaTurnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.SKIPTURN));
                guiSwingProxy.sendMessage(clientMessage);
            }
        });
    }

    private void setCombobox() {

        comboBoxPlayerBoard.removeAll();
        comboBoxPlayerBoard.addItem(modelView.getPlayer(localID).getNick());
        for(int j = 0; j < modelView.getPlayers().size(); j ++){
            if(!modelView.getPlayers().get(j).getId().equals(localID)){
                comboBoxPlayerBoard.addItem(modelView.getPlayers().get(j).getNick());
            }
        }

        comboBoxPlayerBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickPlayer = comboBoxPlayerBoard.getSelectedItem().toString();
                for(int j = 0; j < modelView.getPlayers().size(); j ++){
                    if(modelView.getPlayers().get(j).getNick().equals(nickPlayer)){
                        showBoard(modelView.getBoard(modelView.getPlayers().get(j)));
                    }
                }
            }
        });

    }

    private void setIndicator(){

        boolean yourTurn;

        yourTurn = modelView.getPlayer(localID).isYourTurn();
        if(yourTurn){
            labelTurn.setText("E' il tuo turno");
        }
        else {
            labelTurn.setText("Non e' il tuo turno");
        }
        labelSegnalini.setText("Segnalini: "+ modelView.getPlayer(localID).getFavorTokens());
        labelRound.setText("Round: "+ modelView.getRound());
    }

    private void mostraObiettivi(){
        GUISwingObiettivi dialog = new GUISwingObiettivi(modelView.getPublicObjective());
        dialog.pack();
        dialog.setVisible(true);

    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png",false));
            }
        }
    }

    public void setModelView(ModelView modelView){
        this.modelView = modelView;
        setCombobox();
        setIndicator();
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

    private void showBoard(PlayerBoard playerBoard){
        whiteRefreshBoard();
        toolCardRefresh();

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.getRestriction(i,j).isColorRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(((ColorRestriction) playerBoard.getRestriction(i,j)).getColor()) +".png",false));
                }else if(playerBoard.getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) playerBoard.getRestriction(i,j)).getNumber().getInt() +".png",false));
                }
            }
        }

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.containsDie(i,j)){
                    try{
                        ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(playerBoard.getDie(i,j).getColor()) + playerBoard.getDie(i,j).getNumber().getInt() +".png",false));
                    }catch (Exception e){}
                }
            }
        }
    }

    private void refreshBoard(){
        showBoard(modelView.getBoard(modelView.getPlayer(localID)));
    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    private int[] chooseCell(){
        ChooseCell dialog = new ChooseCell(modelView.getBoard(modelView.getPlayer(localID)));
        int[] pos = dialog.getPosition();
        return pos;
    }

    public void mostraRiserva(){
        GUISwingRiserva dialog = new GUISwingRiserva(modelView.getDraftPool());
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
    }

    public JPanel getContentPane(){
        return contentPane;
    }

    public int chooseDraftPoolPosition(){
        ChooseDraftPoolDie chooseDraftPoolDie = new ChooseDraftPoolDie(modelView.getDraftPool());
        int p = chooseDraftPoolDie.getPosition();
        System.out.println(p);
        return p;
    }

    public void normalSugheroMove(){

        int i = chooseDraftPoolPosition();

        int[] position = chooseCell();
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.MOSSASTANDARD, position[0], position[1], i));
        guiSwingProxy.sendMessage(clientMessage);

    }

    public void tenagliaARotelleMove(){
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.TENAGLIAAROTELLE));
        guiSwingProxy.sendMessage(clientMessage);
        normalSugheroMove();
        normalSugheroMove();
    }

    public void martellettoMove(){
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.MARTELLETTO));
        guiSwingProxy.sendMessage(clientMessage);
    }

    public void sgrossatriceMove(){
        int pos = chooseDraftPoolPosition();
        if(pos < modelView.getDraftPool().size()){
            GUIAumentaValoreDialog dialog = new GUIAumentaValoreDialog();
            boolean addOne = dialog.getValue();
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.PINZASGROSSATRICE,pos,addOne));
            guiSwingProxy.sendMessage(clientMessage);
        }else{
            printError("Devi selezionare un dado");
        }
    }

    public void alesatoreEglomiseMove(Tool tool){
        int[] posi = chooseCell();
        int[] posf = chooseCell();

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool,posi[0],posi[1],posf[0],posf[1]));
        guiSwingProxy.sendMessage(clientMessage);
    }

    public void lathekinMove(){
        int[] posi = chooseCell();
        int[] posf = chooseCell();
        printError("Scegli il secondo dado da muovere");
        int[] pos2i = chooseCell();
        int[] pos2f = chooseCell();

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1],new PlayerMove(Tool.LATHEKIN,pos2i[0],pos2i[1],pos2f[0],pos2f[1])));
        guiSwingProxy.sendMessage(clientMessage);
    }

    public void taglierinaManualeMove(){
        int[] posi = chooseCell();
        int[] posf = chooseCell();

        GenericRadio dialog = new GenericRadio("Vuoi spostare un altro dado?");
        if(dialog.getValue()){
            int[] pos2i = chooseCell();
            int[] pos2f = chooseCell();
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1],new PlayerMove(Tool.LATHEKIN,pos2i[0],pos2i[1],pos2f[0],pos2f[1])));
            guiSwingProxy.sendMessage(clientMessage);
        }else{
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1]));
            guiSwingProxy.sendMessage(clientMessage);
        }
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
        GUIMain game = new GUIMain(guiSwingProxy);
        game.setId((long) 123);
        game.setModelView(modelView);
        frame.setContentPane(game.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);

    }

}
