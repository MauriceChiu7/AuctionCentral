import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Observable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * This Panel contains the information of all the auctions a Bidder can bid on.
 * @author MauriceChiu
 */
public class BiddableAuctionsScreen extends Observable {
    private JPanel biddableAuctionScreen;
    private Bidder currBidder;
    private DataControlCenter dataControl;

    public static final int NUM_OF_PIECES_OF_INFO = 2;

    /**
     *
     * @param currBidder
     * @param dataControl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public BiddableAuctionsScreen(Bidder currBidder, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
        this.biddableAuctionScreen = new JPanel(new BorderLayout());
        this.currBidder = currBidder;
        this.dataControl = dataControl;
        this.setupFrame();
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void setupFrame() throws IOException, ClassNotFoundException {
        this.biddableAuctionScreen.add(new JLabel("\tHere are all the auctions you can bid on: "), BorderLayout.NORTH);
        JPanel auctionFrame = new JPanel();
        //JPanel container = new JPanel();
        auctionFrame.add(new JScrollPane(this.getItemTable(currBidder, dataControl)));

        this.biddableAuctionScreen.add(auctionFrame, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        this.biddableAuctionScreen.add(back, BorderLayout.SOUTH);
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(MainScreen_Bidder.BACK);
        });
    }

    /**
     *
     * @param currBidder
     * @param dataControl
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private JTable getItemTable(Bidder currBidder, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
        String[] columns = new String[] {"Organization"};
        final Class[] columnClass = new Class[] {String.class};
        Object[][] auctionList = new Object[dataControl.getAuctionsCurrBidderCanBidOn(currBidder).size()][NUM_OF_PIECES_OF_INFO];
        int counter = 0;
        for (Auction auc : dataControl.getAuctionsCurrBidderCanBidOn(currBidder)) {
            DateTimeFormatter dtformatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            auctionList[counter][NUM_OF_PIECES_OF_INFO-2] = auc.getOrganization();
            auctionList[counter][NUM_OF_PIECES_OF_INFO-1] = auc.getStart().format(dtformatter);

            counter++;
        }

        DefaultTableModel model = new DefaultTableModel(auctionList, columns) {
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
    public JPanel getBiddableAuctionsScreen() {
        this.biddableAuctionScreen.removeAll();
        try {
            setupFrame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return this.biddableAuctionScreen;
    }
}