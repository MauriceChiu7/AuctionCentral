import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Observable;

public class ViewSubmittedAuction_Screen extends Observable {
    private JPanel viewAllAuctions;
    private DataControlCenter dataControl;
    private NPContact currContact;

    public static final int NUM_OF_PIECES_OF_INFO = 5;

    /**
     *
     * @param dcc
     * @param currContact
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ViewSubmittedAuction_Screen (DataControlCenter dcc, NPContact currContact) throws IOException, ClassNotFoundException {
        this.viewAllAuctions = new JPanel(new BorderLayout());
        this.dataControl = dcc;
        this.currContact = currContact;
        this.setElements();

    }

    public JPanel getViewAllAuctionsScreen() throws IOException, ClassNotFoundException {
        this.viewAllAuctions.removeAll();
        this.setElements();
        return this.viewAllAuctions;
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void setElements() throws IOException, ClassNotFoundException {

        JPanel auctionFrame = new JPanel(new GridLayout(dataControl.getSubmittedAuctionsByNPContact(currContact).size(), 1));
        this.viewAllAuctions.add(new JLabel("\t Here are all of your submitted auction requests," +
                " please pick one to view the items: "), BorderLayout.NORTH);
        for (Auction auc : dataControl.getSubmittedAuctionsByNPContact(currContact)){
            DateTimeFormatter dtformatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            JButton but = new JButton(auc.getOrganization() + "\t\tAuction ID: [ " + auc.getAuctionID() + " ] "
                    + "\t\tAuction Date: [  " + auc.getStart().format(dtformatter) + "  ] ");

            but.addActionListener((ActionEvent e) -> {
                JFrame itemsFrame = new JFrame();
                itemsFrame.setLayout(new BorderLayout());
                JPanel container = new JPanel();
                container.add(new JScrollPane(this.getItemTable(auc)));
                itemsFrame.add(new JLabel("\tItems listed by " + auc.getOrganization() + "\t\t(Auction Date: " +
                        auc.getStart().format(dtformatter) + "): "), BorderLayout.NORTH);
                itemsFrame.add(container, BorderLayout.CENTER);
                itemsFrame.pack();
                itemsFrame.setLocationRelativeTo(null);
                itemsFrame.setVisible(true);
            });
            auctionFrame.add(but);
        }

        this.viewAllAuctions.add(auctionFrame, BorderLayout.CENTER);
        JButton back = new JButton("Back");
        this.viewAllAuctions.add(back, BorderLayout.SOUTH);
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(5);
        });

    }

    /**
     *
     * @param auc recieves the auction to create a table with name, quantity, price and description
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

}
