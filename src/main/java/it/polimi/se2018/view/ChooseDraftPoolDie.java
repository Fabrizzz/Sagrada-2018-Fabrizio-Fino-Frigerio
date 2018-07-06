package it.polimi.se2018.view;

import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.utils.enums.Color;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ChooseDraftPoolDie extends JDialog implements MouseListener {
    private JPanel contentPane;
    private JPanel board;
    private Map<Color,String> colorMap = new HashMap<>();
    private DraftPool draftPool;
    public final CountDownLatch latch = new CountDownLatch(1);
    private int p = 0;

    public ChooseDraftPoolDie(DraftPool draftPool) {
        colorMap.put(Color.BLUE,"B");
        colorMap.put(Color.RED,"R");
        colorMap.put(Color.GREEN, "G");
        colorMap.put(Color.YELLOW,"Y");
        colorMap.put(Color.PURPLE, "P");
        setContentPane(contentPane);
        setModal(true);
        this.draftPool = draftPool;

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

        for(int i = 0; i < 9; i ++){
            ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).addMouseListener(this);
        }
        populateBoard(draftPool);
    }

    public int getPosition(){
        this.pack();
        this.setVisible(true);
        try {
            latch.await();
        }catch (InterruptedException e){}

        return p;
    }

    private void onOK() {
        latch.countDown();
    }

    private void onCancel() {
        latch.countDown();
    }

    private void whiteRefresh(){
        for(int i = 0; i < 9; i ++){
            ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/WHITE.png"));
        }
    }

    private void populateBoard(DraftPool draftPool){
        whiteRefresh();

        for(int i = 0; i < draftPool.size(); i ++) {
            try {
                ((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).setIcon(new StretchIcon("src/main/resources/utilsGUI/" + colorToString(draftPool.getDie(i).getColor()) + "" + draftPool.getDie(i).getNumber().getInt() + ".png"));
            } catch (Exception e) {}
        }

    }

    private String colorToString(Color color){
        return colorMap.get(color);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        for(int i = 0; i < draftPool.size(); i ++) {
                if(((JLabel) (((JPanel) board.getComponent(i)).getComponent(0))).equals(source)){
                    p = i;
                    latch.countDown();
                    return;
                }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
