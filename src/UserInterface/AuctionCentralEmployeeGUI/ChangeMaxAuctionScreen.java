import sun.jvm.hotspot.utilities.soql.JSList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Observable;

public class ChangeMaxAuctionScreen extends Observable {
    private JPanel maxAuctionScreen;
    private DataControlCenter dataControl;
    private int possibleFutureValue;

    public ChangeMaxAuctionScreen (DataControlCenter dcc) throws IOException, ClassNotFoundException {
        this.maxAuctionScreen = new JPanel(new BorderLayout());
        this.dataControl = dcc;
        this.setElements();
    }

    public JPanel getMaxAuctionScreen () {
        return this.maxAuctionScreen;
    }

    private void setElements () throws IOException, ClassNotFoundException {
        JButton backBtn = new JButton("Back");
        JButton confirm = new JButton("Confirm");
        JPanel logic = this.getLogicContents();
        JPanel instructions = this.getInstructions();
        this.maxAuctionScreen.add(instructions, BorderLayout.NORTH);
        this.maxAuctionScreen.add(logic, BorderLayout.CENTER);

        JPanel buttonContainer = new JPanel(new GridLayout(2,1 ));
        buttonContainer.add(confirm);
        buttonContainer.add(backBtn);
        this.maxAuctionScreen.add(buttonContainer, BorderLayout.SOUTH);
        this.setButtonBehavior(confirm, backBtn);
    }

    private void setButtonBehavior (JButton confirm, JButton backBtn) {
        confirm.addActionListener((ActionEvent e) -> {

            int prevVal = this.dataControl.getMaxAuctionAllowed();
            this.dataControl.setMaxAuctionAllowed(possibleFutureValue);
            maxAuctionScreen.removeAll();
            JOptionPane.showMessageDialog(maxAuctionScreen, "Value changed from " + prevVal +
                    " to " + possibleFutureValue);
            try {
                setElements();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            maxAuctionScreen.repaint();
            maxAuctionScreen.revalidate();
        });
        backBtn.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(5); // 5 - revert to main screen
        });
    }

    private JPanel getLogicContents () throws IOException, ClassNotFoundException {
        JPanel toSend = new JPanel(new GridLayout(3, 1));
        String indent = "\t\t\t\t\t\t";
        JLabel activeAuctions = new JLabel(indent + "Currently max upcoming auction limit set to: " +
                this.dataControl.getMaxAuctionAllowed());
        toSend.add(activeAuctions);

        JSlider slider = new JSlider(0, 50, dataControl.getMaxAuctionAllowed());
        this.possibleFutureValue = slider.getValue();

        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        toSend.add(slider);
        JLabel label = new JLabel( indent +
                "System will set max upcoming auction limit to: " + slider.getValue());
        toSend.add(label);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                toSend.remove(2);
                possibleFutureValue = slider.getValue();
                toSend.add(new JLabel(indent +
                        "System will set max upcoming auction limit to: " + possibleFutureValue));
                maxAuctionScreen.repaint();
                maxAuctionScreen.revalidate();
            }
        });
        return toSend;
    }

    private JPanel getInstructions () {
        JPanel toSend = new JPanel(new GridLayout(3, 1));
        String indent = "\t\t\t\t\t\t";
        JLabel title = new JLabel("\tInstructions:");
        JLabel condition1 = new JLabel(indent + "1) Select a number");
        JLabel condition2 = new JLabel(indent + "2) Confirm");
        toSend.add(title);
        toSend.add(condition1);
        toSend.add(condition2);
        return toSend;
    }

}
