import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * This is the top level main screen that shows all the options a Bidder have.
 * @author MauriceChiu
 */
public class MainScreen_Bidder extends Observable{
    private JPanel main;
    private Bidder currBidder;
    private DataControlCenter dataControl;

    public static final String VIEW_AUCTIONS_I_CAN_BID = "View all auctions I can bid on";
    public static final String VIEW_ALL_ITEMS_IN_AN_AUCTION = "View all items in an auction";
    public static final String VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_AN_AUCTION = "View all items I have bid on in an auction";
    public static final String VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_ALL_AUCTIONS = "View all items I have bid on in all auctions";
    public static final String BID_FOR_AN_ITEM_IN_AN_AUCTION = "Bid for an item in an auction";
    public static final String BACK = "back";

    public MainScreen_Bidder (Bidder currBidder, DataControlCenter dataControl) {
        this.main = new JPanel(new BorderLayout());
        this.currBidder = currBidder;
        this.dataControl = dataControl;

        JPanel accountDetails = this.getAccountDetails();
        this.main.add(accountDetails, BorderLayout.NORTH);

        JPanel options = this.getOptions();
        this.main.add(options, BorderLayout.CENTER);
    }

    /**
     * @return toSend is a panel containing Bidder's account information.
     */
    private JPanel getAccountDetails() {
        JPanel toSend = new JPanel(new GridLayout(5,1));

        String indent = "\t\t\t\t\t\t";

        JLabel title = new JLabel("\tAccount Details: ");
        JLabel name = new JLabel(indent + "Name: " + currBidder.getName());
        JLabel userName = new JLabel(indent + "Username: " + currBidder.getUsername());
        JLabel status = new JLabel(indent + "Type: Bidder");
        //JLabel balance = new JLabel(indent + "(Debugging purposes only) Account Balance: $" + currBidder.getBalance());

        toSend.add(title);
        toSend.add(name);
        toSend.add(userName);
        toSend.add(status);
        //toSend.add(balance);

        return toSend;
    }

    /**
     * @return toSend is a panel containing all the buttons/options a Bidder have. Actions they can take.
     */
    public JPanel getOptions() {
        JPanel toSend = new JPanel(new GridLayout(6, 1));

        JButton viewAuctionsICanBid = new JButton("View all auctions I can bid on");
        JButton viewAllItemsInAnAuction = new JButton("View all items in an auction");
        JButton viewAllItemsIHaveBidOnInAnAuction = new JButton("View all items I have bid on in an auction");
        JButton viewAllItemsIHaveBidOnInAllAuctions = new JButton("View all items I have bid on in all auctions");
        JButton bidForAnItemInAnAuction = new JButton("Bid for an item in an auction");
        JButton logout = new JButton("Logout");

        viewAuctionsICanBid.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.VIEW_AUCTIONS_I_CAN_BID);
        });
        viewAllItemsInAnAuction.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.VIEW_ALL_ITEMS_IN_AN_AUCTION);
        });
        viewAllItemsIHaveBidOnInAnAuction.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_AN_AUCTION);
        });
        viewAllItemsIHaveBidOnInAllAuctions.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_ALL_AUCTIONS);
        });
        bidForAnItemInAnAuction.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.BID_FOR_AN_ITEM_IN_AN_AUCTION);
        });
        logout.addActionListener((ActionEvent e) -> {
            try {
                this.dataControl.logOutBidder(currBidder);
                //this.dataControl.logOutNP();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        });

        toSend.add(viewAuctionsICanBid);
        toSend.add(viewAllItemsInAnAuction);
        toSend.add(viewAllItemsIHaveBidOnInAnAuction);
        toSend.add(viewAllItemsIHaveBidOnInAllAuctions);
        toSend.add(bidForAnItemInAnAuction);
        toSend.add(logout);

        Map<Integer, ArrayList<Bid>> numOfBidsByAuctionID = new HashMap<>();

        for (Bid bid : currBidder.getBids()) {
            if (!numOfBidsByAuctionID.containsKey(bid.getAuctionID())) {
                ArrayList<Bid> bids = new ArrayList<>();
                bids.add(bid);
                numOfBidsByAuctionID.put(bid.getAuctionID(), bids);
            } else {
                numOfBidsByAuctionID.get(bid.getAuctionID()).add(bid);
                System.out.println(numOfBidsByAuctionID.get(bid.getAuctionID()));
                System.out.println(bid.getAuctionID());
            }
        }
        int bidCount = 0;
        try {

            for (Auction auc : dataControl.getAuctionsCurrBidderCanBidOn(currBidder)) {
                if (numOfBidsByAuctionID.containsKey(auc.getAuctionID())) {
                    bidCount += numOfBidsByAuctionID.get(auc.getAuctionID()).size();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bidCount >= Bidder.MAX_ITEMS_WITH_BID_IN_ALL_AUCTIONS) {
            bidForAnItemInAnAuction.setText("<html> <center>" + "Bid for an item in an auction" + "<br>" + "(You have reached maximum number of bids for all future auctions)" + "</html>");
            bidForAnItemInAnAuction.setEnabled(false);
        }

        return toSend;
    }

    /**
     * @return this.main contains account details and user options.
     */
    public JPanel getMainScreen() {
        return this.main;
    }
}
