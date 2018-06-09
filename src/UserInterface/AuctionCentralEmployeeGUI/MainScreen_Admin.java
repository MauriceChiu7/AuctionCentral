import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Observable;

public class MainScreen_Admin extends Observable {
    private JPanel main;
    private AuctionCentralEmployee currAdmin;
    private DataControlCenter dataControl;

    public MainScreen_Admin (AuctionCentralEmployee currAdmin, DataControlCenter dcc) {
        this.main = new JPanel(new BorderLayout());
        this.currAdmin = currAdmin;
        this.dataControl = dcc;

        JPanel accountDetails = this.getAccountDetails();
        this.main.add(accountDetails, BorderLayout.NORTH);

        JPanel options = this.getOptions();
        this.main.add(options, BorderLayout.CENTER);
    }

    private JPanel getOptions() {
        JPanel toSend = new JPanel(new GridLayout(5,1));
        JButton changeMaxAuctions = new JButton("Change max number of upcoming auctions allowed");
        JButton viewAllAuctions = new JButton("View all auctions between start and end dates, inclusive");
        JButton viewAuctionsChrono = new JButton("View in brief all auctions in chronological order");
        JButton cancelAuction = new JButton("Cancel a specific Auction");
        JButton logout = new JButton("Logout");

        this.setBehaviorForButtons(changeMaxAuctions, viewAllAuctions, viewAuctionsChrono, cancelAuction, logout);

        toSend.add(changeMaxAuctions);
        toSend.add(viewAllAuctions);
        toSend.add(viewAuctionsChrono);
        toSend.add(cancelAuction);
        toSend.add(logout);

        return toSend;
    }

    private void setBehaviorForButtons(JButton changeMaxAuctions, JButton viewAllAuctions,
                                       JButton viewAuctionsChrono, JButton cancelAuction, JButton logout) {
        // Go to this link if you don't understand lambda expressions
        /*https://stackoverflow.com/questions/37695456/how-t
        o-replace-anonymous-with-lambda-in-java?utm_medium=organic&utm_s
        ource=google_rich_qa&utm_campaign=google_rich_qa*/

        changeMaxAuctions.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(1); // 1 - Change the frame from main to detailed(change max auction num)
        });
        viewAllAuctions.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(2); // 2 - Change the frame to display all auctions
        });
        viewAuctionsChrono.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(3); // 3 - Change frame to display auctions in chrono ordering
        });
        cancelAuction.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(4); // 4 - Change frame to cancel an auction
        });
        logout.addActionListener((ActionEvent e) -> {
            try {
                this.dataControl.logOutAdmin();
            } catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
            System.exit(0);
        });
    }

    private JPanel getAccountDetails() {
        JPanel toSend = new JPanel(new GridLayout(4, 1));
        JLabel title = new JLabel("\tAccount Details:");
        String indent = "\t\t\t\t\t\t";
        JLabel name = new JLabel(indent + "Name: " + currAdmin.getName());
        JLabel userName = new JLabel(indent + "Username: " + currAdmin.getUsername());
        JLabel status = new JLabel(indent + "Type: Auction Central Employee (Admin)");
        toSend.add(title);
        toSend.add(name);
        toSend.add(userName);
        toSend.add(status);
        return toSend;
    }

    public JPanel getMainScreen() {
        return this.main;
    }
}
