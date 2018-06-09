import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class CancelAuctionScreen extends Observable {
    private JPanel cancelAuctionScreen;
    private DataControlCenter dataControl;
    private TableModel dataTable;

    public CancelAuctionScreen(DataControlCenter dcc) throws IOException, ClassNotFoundException {
        this.dataControl = dcc;
        this.cancelAuctionScreen = new JPanel(new BorderLayout());
        this.setElements();
    }

    public JPanel getCancelAuctionScreen() {
        return this.cancelAuctionScreen;
    }

    private void setElements() throws IOException, ClassNotFoundException {
        JPanel instr = this.getInstructions();
        this.cancelAuctionScreen.add(instr, BorderLayout.NORTH);
        this.cancelAuctionScreen.add(this.getButtonContainer(), BorderLayout.SOUTH);
        this.cancelAuctionScreen.add(new JScrollPane(
                this.getAuctionTable(this.dataControl.getCancelAbleAuctions())), BorderLayout.CENTER);
    }

    private JPanel getInstructions () {
        JPanel toSend = new JPanel(new GridLayout(3, 1));
        String indent = "\t\t\t\t\t\t";
        JLabel title = new JLabel("\tInstructions:");
        JLabel condition1 = new JLabel(indent + "1) Check all auctions you" +
                " want to cancel (You can only cancel auctions with 0 bids)");
        JLabel condition2 = new JLabel(indent + "2) Confirm");
        toSend.add(title);
        toSend.add(condition1);
        toSend.add(condition2);
        return toSend;
    }

    private JPanel getButtonContainer() {
        JPanel toSend = new JPanel(new GridLayout(2,1));
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < this.dataTable.getRowCount(); i++) {
                if ((Boolean) this.dataTable.getValueAt(i, 0)) {
                    Integer cancelAuctionId = (Integer) this.dataTable.getValueAt(i, 2);
                    try {
                        Auction toCancel = this.dataControl.getAuctionById(cancelAuctionId);
                        this.dataControl.cancelAuction(toCancel);
                    } catch (IOException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            cancelAuctionScreen.removeAll();
            try {
                setElements();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            cancelAuctionScreen.repaint();
            cancelAuctionScreen.revalidate();
        });
        JButton back = new JButton("Back");
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(5); // 5 - revert to main menu
        });
        toSend.add(confirm);
        toSend.add(back);
        return toSend;
    }

    private JTable getAuctionTable(HashSet<Auction> auctions) {
        String[] columns = new String[] {"Cancel?", "Number of Bids", "ID", "Auction Name", "Start Date", "End Date"};
        final Class[] columnClass = new Class[] {
                Boolean.class, String.class, String.class, String.class, String.class, String.class
        };
        Object[][] auctionTiming = new Object[auctions.size()][6];
        int counter = 0;
        for (Auction auction : this.dataControl.sortAuctionSet(auctions)) {
            auctionTiming[counter][0] = false;
            int bidCount = 0;
            for (Item item : auction.getItems()) {
                bidCount += item.getBids().size();
            }
            auctionTiming[counter][1] = "" + bidCount;
            auctionTiming[counter][2] = auction.getAuctionID();
            auctionTiming[counter][3] = auction.getOrganization();
            auctionTiming[counter][4] = auction.getStart().toString();
            auctionTiming[counter][5] = auction.getEnd().toString();
            counter++;
        }
        DefaultTableModel model = new DefaultTableModel(auctionTiming, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                HashSet<Integer> areEditable = new HashSet<>();
                for (int i = 0; i < this.getRowCount(); i++) {
                    if (Integer.parseInt((String)this.getValueAt(i, 1)) == 0) {
                        areEditable.add(i);
                    }
                }
                return areEditable.contains(row);
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };
        JTable table = new JTable(model);
        this.dataTable = table.getModel();
        return table;
    }
}
