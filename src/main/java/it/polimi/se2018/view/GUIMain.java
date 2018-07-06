package it.polimi.se2018.view;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.EmptyBagException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

public class GUIMain  implements MouseListener{
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
    private JLabel privateObjectiveLabel;
    private Map<Tool,String> toolCardMap = new HashMap<>();
    private ModelView modelView;
    private Long localID;
    private GUISwingProxy guiSwingProxy;
    private boolean initialPack = false;

    protected GUIMain(GUISwingProxy guiSwingProxy){
        this.guiSwingProxy = guiSwingProxy;;
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

        tool1Label.addMouseListener(this);
        tool2Label.addMouseListener(this);
        tool3Label.addMouseListener(this);

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
                normalSugheroMove(Tool.MOSSASTANDARD);
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

        comboBoxPlayerBoard.removeAllItems();
        comboBoxPlayerBoard.addItem(modelView.getPlayer(localID).getNick());
        for(int j = 0; j < modelView.getPlayers().size(); j ++){
            if(!modelView.getPlayers().get(j).getId().equals(localID)){
                comboBoxPlayerBoard.addItem(modelView.getPlayers().get(j).getNick());
            }
        }

        comboBoxPlayerBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickPlayer = (String) comboBoxPlayerBoard.getSelectedItem();
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
        String color;

        yourTurn = modelView.getPlayer(localID).isYourTurn();
        if(yourTurn){
            labelTurn.setText("E' il tuo turno");
        }
        else {
            labelTurn.setText("Non e' il tuo turno");
        }
        labelSegnalini.setText("Segnalini: "+ modelView.getPlayer(localID).getFavorTokens());
        labelRound.setText("Round: "+ modelView.getRound());
        privateObjectiveLabel.setText("Obiettivo privato: "+ modelView.getPrivateObjective().getColorString());
    }

    private void mostraObiettivi(){
        ShowObjectives dialog = new ShowObjectives(modelView.getPublicObjective());
        dialog.pack();
        dialog.setVisible(true);

    }

    private void whiteRefreshBoard(){
        for(int i = 0; i < 4; i ++){
            for(int j = 0; j < 5; j++){
                ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
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

    protected void chooseBoard(Board[] boards){
        ChooseBoard dialog = new ChooseBoard(boards,this);
        dialog.pack();
        dialog.setVisible(true);
    }

    private int[] chooseCell(String message){
        ChooseCell dialog = new ChooseCell(modelView.getBoard(modelView.getPlayer(localID)),message);
        int[] pos = dialog.getPosition();
        return pos;
    }

    private int[] chooseRoundTrackDie(){
        ChooseRoundTrackDie dialog = new ChooseRoundTrackDie(modelView.getRoundTrack());
        int[] pos = dialog.getPosition();
        return pos;
    }

    private NumberEnum chooseNewValue(){
        ChooseNewValue dialog = new ChooseNewValue();
        return dialog.getNewValue();
    }

    protected void selectedBoard(Board board){
        guiSwingProxy.selectedBoard(board);
    }

    public ModelView getModelView(){
        return modelView;
    }

    protected void endGame(ServerMessage message){
        EndGame dialog = new EndGame(message);
        dialog.pack();
        dialog.setLocationRelativeTo( null );
        dialog.setVisible(true);
        System.exit(0);
    }

    protected void printError(String error){
        JOptionPane.showMessageDialog(this.contentPane, error);
    }

    private void mostraTracciatoDadi(){
        if(modelView != null) {
            ShowRoundTrack dialog = new ShowRoundTrack(modelView.getRoundTrack());
            dialog.pack();
            dialog.setVisible(true);
        }else{
            ShowRoundTrack dialog = new ShowRoundTrack(new RoundTrack());
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
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(((ColorRestriction) playerBoard.getRestriction(i,j)).getColor()) +".png"));
                }else if(playerBoard.getRestriction(i,j).isNumberRestriction()){
                    ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/numberRestriction" + ((NumberRestriction) playerBoard.getRestriction(i,j)).getNumber().getInt() +".png"));
                }
            }
        }

        for(int i = 0; i < 4; i ++) {
            for (int j = 0; j < 5; j++) {
                if(playerBoard.containsDie(i,j)){
                    try{
                        ((JLabel) (((JPanel) board.getComponent(j + 5 * i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + GUIUtils.colorToString(playerBoard.getDie(i,j).getColor()) + playerBoard.getDie(i,j).getNumber().getInt() +".png"));
                    }catch (Exception e){}
                }
            }
        }

        if(!initialPack) {
            try{
                ((JFrame) SwingUtilities.getWindowAncestor(contentPane)).pack();
            }catch (NullPointerException e){}
            initialPack = true;
        }
    }

    private void refreshBoard(){
        showBoard(modelView.getBoard(modelView.getPlayer(localID)));
    }

    private void mostraRiserva(){
        ShowDraftPool dialog = new ShowDraftPool(modelView.getDraftPool());
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onCancel() {
        System.exit(0);
    }

    public JPanel getContentPane(){
        return contentPane;
    }

    private int chooseDraftPoolPosition(){
        ChooseDraftPoolDie chooseDraftPoolDie = new ChooseDraftPoolDie(modelView.getDraftPool());
        int p = chooseDraftPoolDie.getPosition();
        chooseDraftPoolDie.dispose();
        return p;
    }

    private void normalSugheroMove(Tool tool){
        int i = chooseDraftPoolPosition();

        int[] position = chooseCell("Scegli dove piazzare il dado");
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], i));
        guiSwingProxy.sendMessage(clientMessage);

    }

    private void tenagliaARotelleMove(){
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.TENAGLIAAROTELLE));
        guiSwingProxy.sendMessage(clientMessage);
        normalSugheroMove(Tool.MOSSASTANDARD);
        normalSugheroMove(Tool.MOSSASTANDARD);
    }

    private void martellettoMove(){
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.MARTELLETTO));
        guiSwingProxy.sendMessage(clientMessage);
    }

    private void sgrossatriceMove(){
        int pos = chooseDraftPoolPosition();
        if(pos < modelView.getDraftPool().size()){
            addDieValue dialog = new addDieValue();
            boolean addOne = dialog.getValue();
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.PINZASGROSSATRICE,pos,addOne));
            guiSwingProxy.sendMessage(clientMessage);
        }else{
            printError("Devi selezionare un dado");
        }
    }

    private void alesatoreEglomiseMove(Tool tool){
        int[] posi = chooseCell("Scegli il dado da muovere");
        int[] posf = chooseCell("Scegli dove piazzare il dado");

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool,posi[0],posi[1],posf[0],posf[1]));
        guiSwingProxy.sendMessage(clientMessage);
    }

    private void lathekinMove(){
        int[] posi = chooseCell("Scegli il primo dado da muovere");
        int[] posf = chooseCell("Scegli dove piazzare il dado");
        printError("Scegli il secondo dado da muovere");
        int[] pos2i = chooseCell("Scegli il secondo dado da muovere");
        int[] pos2f = chooseCell("Scegli dove piazzare il dado");

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1],new PlayerMove(Tool.LATHEKIN,pos2i[0],pos2i[1],pos2f[0],pos2f[1])));
        guiSwingProxy.sendMessage(clientMessage);
    }

    private void taglierinaManualeMove(){
        int[] posi = chooseCell("Scegli il primo dado da muovere");
        int[] posf = chooseCell("Scegli dove piazzare il dado");

        GenericRadio dialog = new GenericRadio("Vuoi spostare un altro dado?");
        if(dialog.getValue()){
            int[] pos2i = chooseCell("Scegli il secondo dado da muovere");
            int[] pos2f = chooseCell("Scegli dove piazzare il dado");
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1],new PlayerMove(Tool.LATHEKIN,pos2i[0],pos2i[1],pos2f[0],pos2f[1])));
            guiSwingProxy.sendMessage(clientMessage);
        }else{
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.LATHEKIN,posi[0],posi[1],posf[0],posf[1]));
            guiSwingProxy.sendMessage(clientMessage);
        }
    }

    private void taglierinaCircolareMove(){
        int pos = chooseDraftPoolPosition();
        int[] pos2 = chooseRoundTrackDie();
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(pos,pos2[0],pos2[1],Tool.TAGLIERINACIRCOLARE));
        guiSwingProxy.sendMessage(clientMessage);
    }

    private void pennelloPastaSaldaMove(){
        int i = chooseDraftPoolPosition();
        try{
            Die die = modelView.getDraftPool().getDie(i);
            die.reRoll();
            printError("Valore del nuovo dado: " + die.getNumber().getInt());

            boolean check = true;
            PlayerBoard board = modelView.getBoard(modelView.getPlayer(localID));
            for (int r = 0; r < 4 && check; r++) {
                for (int c = 0; c < 5 && check; c++) {
                    if ((board.verifyInitialPositionRestriction(r, c) && board.isEmpty()) ||
                            (!board.containsDie(r, c) && board.verifyNumberRestriction(die, r, c) &&
                                    board.verifyColorRestriction(die, r, c) &&
                                    board.verifyNearCellsRestriction(die, r, c) &&
                                    board.verifyPositionRestriction(r, c)))
                        check = false;
                }
            }
            ClientMessage clientMessage;
            if (!check) {
                int[] position = chooseCell("Scegli dove piazzare il dado");
                clientMessage = new ClientMessage(new PlayerMove(position[0], position[1], i, die.getNumber(), Tool.PENNELLOPERPASTASALDA));
            } else {
                printError("Il dado viene riposto in riserva");
                clientMessage = new ClientMessage(new PlayerMove(i, die.getNumber(), Tool.PENNELLOPERPASTASALDA));
            }

            guiSwingProxy.sendMessage(clientMessage);
        }catch (NoDieException e){
            printError("Errore, dado non presente nella cella scelta");
        }
    }

    private void diluentePerPastaSaldaMove(){
        int i = chooseDraftPoolPosition();
        try{
            Die die = modelView.getDraftPool().getDie(i);
            Die newDie = modelView.getDiceBag().getFirst();
            ShowDie dialog = new ShowDie("src/main/resources/utilsGUI/" + GUIUtils.colorToString(newDie.getColor()) + newDie.getNumber().getInt() +".png");
            NumberEnum newValue = chooseNewValue();

            boolean check = true;
            PlayerBoard board = modelView.getBoard(modelView.getPlayer(localID));
            for (int r = 0; r < 4 && check; r++) {
                for (int c = 0; c < 5 && check; c++) {
                    if ((board.verifyInitialPositionRestriction(r, c) && board.isEmpty()) ||
                            (!board.containsDie(r, c) &&
                                    board.verifyNumberRestriction(die, r, c) &&
                                    board.verifyColorRestriction(die, r, c) &&
                                    board.verifyNearCellsRestriction(die, r, c) &&
                                    board.verifyPositionRestriction(r, c)))
                        check = false;
                }
            }
            if (check) {
                printError("Non è possibile posizionarlo, viene riposto in riserva");
                guiSwingProxy.sendMessage(new ClientMessage(new PlayerMove(i, newValue, Tool.DILUENTEPERPASTASALDA)));
            } else {
                int[] pair = chooseCell("Scegli dove piazzare il dado");
                guiSwingProxy.sendMessage(new ClientMessage(new PlayerMove(pair[0], pair[1], i, newValue, Tool.DILUENTEPERPASTASALDA)));
            }
        }catch (NoDieException e){
            printError("Errore, dado non presente nella cella scelta");
        }catch (EmptyBagException e){
            printError("Errore, nessun dado presente nel sacchetto");
        }
    }

    private void tamponeDiamantatoMove(){
        int i = chooseDraftPoolPosition();
        try{
            modelView.getDraftPool().getDie(i);
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.TAMPONEDIAMANTATO,i));
            guiSwingProxy.sendMessage(clientMessage);
        }catch (NoDieException e){
            printError("Nessun dado presente nella cella selezionata");
        }

    }

    public void move(Tool tool){
        switch (tool) {
            case MOSSASTANDARD:
                break;
            case RIGAINSUGHERO:
                normalSugheroMove(tool);
                break;
            case SKIPTURN:
                break;
            case MARTELLETTO:
                martellettoMove();
                break;
            case TENAGLIAAROTELLE:
                tenagliaARotelleMove();
                break;
            case PINZASGROSSATRICE:
                sgrossatriceMove();
                break;
            case PENNELLOPEREGLOMISE:
                alesatoreEglomiseMove(tool);
                break;
            case ALESATOREPERLAMINADIRAME:
                alesatoreEglomiseMove(tool);
                break;
            case TAGLIERINAMANUALE:
                taglierinaManualeMove();
                break;
            case LATHEKIN:
                lathekinMove();
                break;
            case TAGLIERINACIRCOLARE:
                taglierinaCircolareMove();
                break;
            case PENNELLOPERPASTASALDA:
                pennelloPastaSaldaMove();
                break;
            case DILUENTEPERPASTASALDA:
                diluentePerPastaSaldaMove();
                break;
            case TAMPONEDIAMANTATO:
                tamponeDiamantatoMove();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        Tool[] tools = modelView.getTools().keySet().toArray(new Tool[modelView.getTools().keySet().size()]);
        if(source.equals(tool1Label)){
            move(tools[0]);
        }else if(source.equals(tool2Label)){
            move(tools[1]);
        }else if(source.equals(tool3Label)){
            move(tools[2]);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        Model model = ModelControllerInitializerTest.initialize(Tool.LATHEKIN);
        model.getRoundTrack().addDie(0,new Die(Color.RED));
        model.getRoundTrack().addDie(0,new Die(Color.BLUE));
        model.getRoundTrack().addDie(0,new Die(Color.YELLOW));
        model.getDraftPool().addDie(new Die(Color.RED));
        model.getDraftPool().addDie(new Die(Color.BLUE));

        ModelView modelView = new ModelView(model,new PrivateObjective(Color.RED));

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
