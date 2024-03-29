package it.polimi.se2018.view;

import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Tool;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Matteo
 */
public class ShowObjectives extends JDialog {
    private JPanel contentPane;
    private JLabel objective1;
    private JLabel objective2;
    private JLabel objective3;
    private JPanel board;
    List<PublicObjective> publicObjective;

    /**
     * Costructor
     * @param publicObjective
     */
    protected ShowObjectives(List<PublicObjective> publicObjective) {

        this.publicObjective = publicObjective;
        setContentPane(contentPane);
        setModal(true);

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
        
        showObjectiveCard();
    }

    /**
     * Show the objective card
     */
    private void showObjectiveCard() {
        String name;

        name = ObjectiveCardNamePNG(publicObjective.get(0));
        objective1.setIcon(new StretchIcon("src/main/resources/publicObjective/"+name+".png"));
        name = ObjectiveCardNamePNG(publicObjective.get(1));
        objective2.setIcon(new StretchIcon("src/main/resources/publicObjective/"+name+".png"));
        name = ObjectiveCardNamePNG(publicObjective.get(2));
        objective3.setIcon(new StretchIcon("src/main/resources/publicObjective/"+name+".png"));

    }

    /**
     * convert the objective card in the string
     * @param objectiveCard
     * @return
     */
    private String ObjectiveCardNamePNG(PublicObjective objectiveCard){

        switch (objectiveCard.getObjectiveName()){
            case COLORIDIVERSIRIGA:
                return "publicObjective01";
            case COLORIDIVERSICOLONNA:
                return "publicObjective02";
            case SFUMATUREDIVERSERIGA:
                return "publicObjective03";
            case SFUMATUREDIVERSECOLONNA:
                return "publicObjective04";
            case SFUMATURECHIARE:
                return "publicObjective05";
            case SFUMATUREMEDIE:
                return "publicObjective06";
            case SFUMATURESCURE:
                return "publicObjective07";
            case SFUMATUREDIVERSE:
                return "publicObjective08";
            case DIAGONALICOLORATE:
                return "publicObjective09";
            case VARIETADICOLORE:
                return "publicObjective10";
            default:
                return null;
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Model model = ModelControllerInitializerTest.initialize(Tool.LATHEKIN);
        ModelView modelView = new ModelView(model);
        ShowObjectives dialog = new ShowObjectives(modelView.getPublicObjective());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
