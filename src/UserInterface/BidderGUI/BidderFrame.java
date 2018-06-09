import sun.applet.Main;

import javax.swing.*;
import java.util.HashSet;
import java.util.Observer;
import java.awt.*;
import javax.swing.*;
import java.util.Observable;
import java.io.IOException;

/**
 * This is the GUI Frame for Bidder
 * @author MauriceChiu
 */
public class BidderFrame implements Observer{
    private Bidder currBidder;
    private DataControlCenter dataControl;
    private JFrame frame;
    private MainScreen_Bidder main;
    private BiddableAuctionsScreen biddableAuctionsScreen;
    private AllItemsInAuctionScreen itemsInAuctionScreen;
    private BidItemsInAnAuctionScreen bidItemsInAnAuctionScreen;
    private BidItemsInAllAuctionScreen bidItemsInAllAuctionScreen;
    private PlacingABidScreen placingABidScreen;
    public static HashSet<Auction> auctionsCurrBidderCanBidOn;

    public BidderFrame(Bidder currBidder, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
        this.currBidder = currBidder;
        this.dataControl = dataControl;
        this.dataControl.linkBidItemsWithAuctionID(this.currBidder);

        auctionsCurrBidderCanBidOn = dataControl.getAuctionsCurrBidderCanBidOn(currBidder);

        this.frame = new JFrame();
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Bidder - " + currBidder.getName() + " - Main menu");

        this.main = new MainScreen_Bidder(currBidder, dataControl);
        this.main.addObserver(this);

        this.biddableAuctionsScreen = new BiddableAuctionsScreen(currBidder, dataControl);
        this.biddableAuctionsScreen.addObserver(this);

        this.itemsInAuctionScreen = new AllItemsInAuctionScreen(currBidder, dataControl);
        this.itemsInAuctionScreen.addObserver(this);

        this.bidItemsInAnAuctionScreen = new BidItemsInAnAuctionScreen(currBidder, dataControl);
        this.bidItemsInAnAuctionScreen.addObserver(this);

        this.bidItemsInAllAuctionScreen = new BidItemsInAllAuctionScreen(currBidder, dataControl);
        this.bidItemsInAllAuctionScreen.addObserver(this);

        this.placingABidScreen = new PlacingABidScreen(currBidder, dataControl);
        this.placingABidScreen.addObserver(this);

        this.frame.setSize(400,500);

        this.frame.add(main.getMainScreen(), BorderLayout.CENTER);

        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    @Override
    public void update(Observable parent, Object arg) {
        String result = (String) arg;
        if (result == MainScreen_Bidder.VIEW_AUCTIONS_I_CAN_BID) {
            this.frame.getContentPane().removeAll();
            try {
                this.biddableAuctionsScreen = new BiddableAuctionsScreen(currBidder, dataControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.biddableAuctionsScreen.addObserver(this);
            this.frame.add(this.biddableAuctionsScreen.getBiddableAuctionsScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - " + MainScreen_Bidder.VIEW_AUCTIONS_I_CAN_BID);
            this.frame.pack();
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == MainScreen_Bidder.VIEW_ALL_ITEMS_IN_AN_AUCTION) {
            this.frame.getContentPane().removeAll();
            try {
                this.itemsInAuctionScreen = new AllItemsInAuctionScreen(currBidder, dataControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.itemsInAuctionScreen.addObserver(this);
            this.frame.add(this.itemsInAuctionScreen.getItemsInAuctionScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - " + MainScreen_Bidder.VIEW_ALL_ITEMS_IN_AN_AUCTION);
            this.frame.pack();
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_AN_AUCTION) {
            this.frame.getContentPane().removeAll();
            try {
                this.bidItemsInAnAuctionScreen = new BidItemsInAnAuctionScreen(currBidder, dataControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.bidItemsInAnAuctionScreen.addObserver(this);
            this.frame.add(this.bidItemsInAnAuctionScreen.getItemsInAuctionScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - " + MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_AN_AUCTION);
            this.frame.pack();
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_ALL_AUCTIONS) {
            this.frame.getContentPane().removeAll();
            try {
                this.bidItemsInAllAuctionScreen = new BidItemsInAllAuctionScreen(currBidder, dataControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.bidItemsInAllAuctionScreen.addObserver(this);
            this.frame.add(this.bidItemsInAllAuctionScreen.getItemsInAllAuctionScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - " + MainScreen_Bidder.VIEW_ALL_ITEMS_I_HAVE_BID_ON_IN_ALL_AUCTIONS);
            this.frame.pack();
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == MainScreen_Bidder.BID_FOR_AN_ITEM_IN_AN_AUCTION) {
            this.frame.getContentPane().removeAll();
            try {
                this.placingABidScreen = new PlacingABidScreen(currBidder, dataControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.placingABidScreen.addObserver(this);
            this.frame.add(this.placingABidScreen.getPlacingABidScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - " + MainScreen_Bidder.BID_FOR_AN_ITEM_IN_AN_AUCTION);
            this.frame.pack();
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == MainScreen_Bidder.BACK) {
            this.frame.getContentPane().removeAll();
            this.main = new MainScreen_Bidder(currBidder, dataControl);
            this.main.addObserver(this);
            this.frame.add(this.main.getMainScreen(), BorderLayout.CENTER);
            this.frame.setTitle("Bidder - " + currBidder.getName() + " - Main menu");
            this.frame.setSize(400, 500);
            this.frame.repaint();
            this.frame.revalidate();
        }
    }
}
