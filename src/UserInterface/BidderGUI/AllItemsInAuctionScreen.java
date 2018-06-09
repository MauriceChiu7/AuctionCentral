import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Observable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * This Panel displays the items of an auction chosen by the Bidder.
 * @author MauriceChiu
 * @version May 29, 2018
 */
public class AllItemsInAuctionScreen extends Observable {
    private JPanel itemsInAuctionScreen;
    private Bidder currBidder;
    private DataControlCenter dataControl;

    public static final int NUM_OF_PIECES_OF_INFO = 5;
    //public static final String VIEW_ALL_ITEMS_I_CAN_BID = "View all items I can bid on";

    /**
     *
     * @param currBidder
     * @param dataControl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AllItemsInAuctionScreen (Bidder currBidder, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
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
        JPanel auctionFrame = new JPanel(new GridLayout(dataControl.getAllAuctions().size(), 1));
        this.itemsInAuctionScreen.add(new JLabel("\tHere are all the auctions, please pick one to view the items: "), BorderLayout.NORTH);
        for (Auction auc : dataControl.getAllAuctions()){
            DateTimeFormatter dtformatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            JButton but = new JButton(auc.getOrganization() + "\t\t(Auction Date: " + auc.getStart().format(dtformatter) + ")");
            but.addActionListener((ActionEvent e) -> {
                JFrame itemsFrame = new JFrame();
                itemsFrame.setLayout(new BorderLayout());

                JPanel container = new JPanel();
                container.add(new JScrollPane(this.getItemTable(auc)));

                itemsFrame.add(new JLabel("\tItems listed by " + auc.getOrganization() + "\t\t(Auction Date: " + auc.getStart().format(dtformatter) + "): "), BorderLayout.NORTH);
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

        //System.out.println("setupAuctions\tNumber of auctions: " + dataControl.getAuctionsCurrBidderCanBidOn(currBidder).size());
        //return auctionFrame;
    }

    /**
     *
     * @param auc
     * @return
     */
    private JTable getItemTable(Auction auc) {
        String[] columns = new String[] {"Item Name", "Bulk Quantity", "Bid Price", "Description", "Image"};
        final Class[] columnClass = new Class[] {String.class, String.class, String.class, String.class, String.class};
        Object[][] itemList = new Object[auc.getItems().size()][NUM_OF_PIECES_OF_INFO];
        int counter = 0;
        for (Item item : auc.getItems()) {
            itemList[counter][NUM_OF_PIECES_OF_INFO-5] = item.getName();
            itemList[counter][NUM_OF_PIECES_OF_INFO-4] = item.getQuantity();
            itemList[counter][NUM_OF_PIECES_OF_INFO-3] = "$" + item.getCurrentBid();
            itemList[counter][NUM_OF_PIECES_OF_INFO-2] = item.getDescription();
            itemList[counter][NUM_OF_PIECES_OF_INFO-1] = item.getImagePath();
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void update() throws IOException, ClassNotFoundException {
        setupAuctions();
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