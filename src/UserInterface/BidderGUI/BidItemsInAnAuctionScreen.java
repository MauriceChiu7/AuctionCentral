import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

/**
 * This Panel displays the items that the Bidder has bid on in an auction.
 * @author MauriceChiu
 */
public class BidItemsInAnAuctionScreen extends Observable {
    private JPanel itemsInAuctionScreen;
    private Bidder currBidder;
    private DataControlCenter dataControl;

    public static final int NUM_OF_PIECES_OF_INFO = 3;

    /**
     *
     * @param currBidder
     * @param dataControl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public BidItemsInAnAuctionScreen (Bidder currBidder, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
        //System.out.println("Instance of BiddableAuctionsScreen created.");
        this.itemsInAuctionScreen = new JPanel(new BorderLayout());
        this.currBidder = currBidder;
        this.dataControl = dataControl;
        this.setupAuctions();
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void setupAuctions() throws IOException, ClassNotFoundException {
        JPanel auctionFrame = new JPanel(new GridLayout(dataControl.getAuctionsCurrBidderCanBidOn(currBidder).size(), 1));
        this.itemsInAuctionScreen.add(new JLabel("\tHere are all the auctions, please pick one to view the items that you have bid on: "), BorderLayout.NORTH);

        HashSet<Auction> auctionsCurrBidderHasBidsOn = new HashSet<>();
        for (Bid bid : currBidder.getBids()){
            auctionsCurrBidderHasBidsOn.add(dataControl.getAuctionById(bid.getAuctionID()));
        }

        for (Auction auc : auctionsCurrBidderHasBidsOn) {
        //for (Auction auc : dataControl.getAuctionsCurrBidderHasBids(currBidder)){
        //int checkID = currBidder.getBids().get(0).getAuctionID();
        //for (Bid bid : currBidder.getBids()) {

            DateTimeFormatter dtformatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            JButton but = new JButton(auc.getOrganization() + "\t\t(Auction Date: " + auc.getStart().format(dtformatter) + ")");
            but.addActionListener((ActionEvent e) -> {
                JFrame itemsFrame = new JFrame();
                itemsFrame.setLayout(new BorderLayout());

                JPanel container = new JPanel();
                try {
                    container.add(new JScrollPane(this.getItemTable(auc)));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                itemsFrame.add(new JLabel("\tHere are all the items you have bid on with " + auc.getOrganization() + ": "), BorderLayout.NORTH);
                itemsFrame.add(container, BorderLayout.CENTER);
                itemsFrame.pack();
                itemsFrame.setLocationRelativeTo(null);
                itemsFrame.setVisible(true);
            });
            auctionFrame.add(but);
        }

        this.itemsInAuctionScreen.add(auctionFrame, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        this.itemsInAuctionScreen.add(back, BorderLayout.SOUTH);
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.BACK);
        });
    }

    /**
     *
     * @param auc
     * @return
     */
    private JTable getItemTable(Auction auc) throws IOException, ClassNotFoundException {
        ArrayList<Bid> bidsList = new ArrayList<>();
        for (Bid bid : currBidder.getBids()) {
            if (bid.getAuctionID() == auc.getAuctionID()) {
                bidsList.add(bid);
            }
        }

        String[] columns = new String[] {"Item Name", "Your Bid Price", "Auction Name"};
        final Class[] columnClass = new Class[] {String.class, String.class, String.class};
        Object[][] itemList = new Object[bidsList.size()][NUM_OF_PIECES_OF_INFO];

        int counter = 0;
        for (Bid bid : bidsList) {
            //if (bid.getAuctionID() == auc.getAuctionID()) {
                //System.out.println(bid.getItem()); // TODO remove
                itemList[counter][NUM_OF_PIECES_OF_INFO - 3] = bid.getItem();
                itemList[counter][NUM_OF_PIECES_OF_INFO - 2] = "$" + bid.getAmount();
                Auction theAuction = dataControl.getAuctionNameByItem(bid.getItem());
                itemList[counter][NUM_OF_PIECES_OF_INFO - 1] = theAuction.getOrganization();
            //}
            counter++;
        }
        DefaultTableModel model = new DefaultTableModel(itemList, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };
        return new JTable(model);
    }

    /**
     *
     * @return
     */
    public JPanel getItemsInAuctionScreen() {
        this.itemsInAuctionScreen.removeAll();
        try {
            setupAuctions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this.itemsInAuctionScreen;
    }
}