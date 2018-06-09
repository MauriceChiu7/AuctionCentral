import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Observable;

public class NPContact_MainScreen extends Observable {

    private JPanel main;
    private NPContact currContact;
    private DataControlCenter dataControl;

    /**
     *
     * @param currContact
     * @param dataControl
     */
    public NPContact_MainScreen(NPContact currContact, DataControlCenter dataControl) {

        this.main = new JPanel(new BorderLayout());
        this.currContact = currContact;
        this.dataControl = dataControl;

        JPanel accountDetails = this.getAccountDetails();
        this.main.add(accountDetails, BorderLayout.NORTH);

        JPanel options = this.getOptions();
        this.main.add(options, BorderLayout.CENTER);
    }

    /**
     *
     * Sets the User's account detail bi giving the name, user type
     */
    private JPanel getAccountDetails() {
        JPanel toSend = new JPanel(new GridLayout(5, 2, 20, 20));
        JLabel title = new JLabel("\tAccount Details:");
        String indent = "\t\t\t\t\t\t";
        JLabel name = new JLabel(indent + "Name: " + currContact.getName());
        JLabel userName = new JLabel(indent + "Username: " + currContact.getUsername());
        JLabel status = new JLabel(indent + "Type: None Profit Organization Contact Person");
        toSend.add(title);
        toSend.add(name);
        toSend.add(userName);
        toSend.add(status);
        return toSend;
    }

    /**
     *
     * Sets up the screen with three buttons which corresponds to a user story
     */
    private JPanel getOptions() {

        JPanel toSend = new JPanel(new GridLayout(4,2, 25, 25));
        JButton viewAllAuctions = new JButton("View all submitted auction requests");
        JButton submitAuctions = new JButton("Submit an auction request");
        submitAuctions.setEnabled(dataControl.isAuctionAllowed());
        JButton logout = new JButton("Logout");


        this.setBehaviorForButtons(viewAllAuctions, submitAuctions, logout);

        toSend.add(viewAllAuctions);
        toSend.add(submitAuctions);
        toSend.add(logout);

        return toSend;
    }

    /**
     *
     * @param viewAllAuctions button switches the screen to a different window
     * @param submitAuctions button switches the screen to a different window
     * @param logout button switches the screen to a different window
     */
    private void setBehaviorForButtons(JButton viewAllAuctions, JButton submitAuctions, JButton logout) {

        viewAllAuctions.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(1); // 1 - Change the frame to display all auctions
        });
        submitAuctions.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(2); // 2 - Change frame to submit auctions
        });

        logout.addActionListener((ActionEvent e) -> {
            try {
                this.dataControl.logOutNP();
            } catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
            System.exit(0);
        });
    }

    public JPanel getMainScreen() {
        return this.main;
    }
}
