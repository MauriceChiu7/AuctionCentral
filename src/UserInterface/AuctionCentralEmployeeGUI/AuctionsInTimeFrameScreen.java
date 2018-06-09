import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

/**
 * @author Baisal
 * @version May 29, 2018
 */
public class AuctionsInTimeFrameScreen extends Observable {
    private JPanel auctionsInTimeFramePanel;
    private DataControlCenter dataControl;
    private JTextField start;
    private JTextField end;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private HashSet<Auction> auctionInFrame;

    /**
     *
     * @param dcc
     */
    public AuctionsInTimeFrameScreen (DataControlCenter dcc) {
        this.dataControl = dcc;
        this.auctionsInTimeFramePanel = new JPanel(new BorderLayout());
        this.start = new JTextField(12);
        this.end = new JTextField(12);
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
        this.auctionInFrame = new HashSet<>();
        this.setElements();
    }

    /**
     *
     */
    private void setElements() {
        JPanel instr = this.getInstructions();
        this.auctionsInTimeFramePanel.add(instr, BorderLayout.NORTH);

        JPanel centerContainer =this.getDatePanel();
        this.auctionsInTimeFramePanel.add(centerContainer, BorderLayout.CENTER);

        JPanel btnContainer = this.getButtonContainer();
        this.auctionsInTimeFramePanel.add(btnContainer, BorderLayout.SOUTH);
    }

    /**
     *
     * @return
     */
    private JPanel getDatePanel() {
        JPanel container = new JPanel(new GridLayout(3,1));

        JPanel firstRow = new JPanel();
        firstRow.add(new JLabel("Start Date"));
        firstRow.add(this.start);
        firstRow.add(new JLabel("Ex. (06/16/1999 06:30 AM)"));

        JPanel secRow = new JPanel();
        secRow.add(new JLabel("  End Date"));
        secRow.add(this.end);
        secRow.add(new JLabel("Ex. (07/16/1999 11:30 PM)"));

        container.add(firstRow);
        container.add(secRow);
        container.add(new JScrollPane(
                this.getAuctionTable(this.auctionInFrame)));

        return container;
    }

    /**
     *
     * @param auctions
     * @return
     */
    private JTable getAuctionTable(HashSet<Auction> auctions) {
        String[] columns = new String[] {"Auction Name", "Auction ID", "Number Items", "Start Date", "End Date"};
        final Class[] columnClass = new Class[] {
                String.class, String.class, String.class, String.class, String.class
        };
        Object[][] auctionTiming = new Object[auctions.size()][5];
        int counter = 0;
        ArrayList<Auction> auctionList = this.dataControl.sortAuctionSet(auctions);
        for (Auction auction : auctionList) {
            auctionTiming[counter][0] = auction.getOrganization();
            auctionTiming[counter][1] = auction.getAuctionID() + "";
            auctionTiming[counter][2] = auction.getItems().size();
            auctionTiming[counter][3] = auction.getStart().toString();
            auctionTiming[counter][4] = auction.getEnd().toString();
            counter++;
        }
        DefaultTableModel model = new DefaultTableModel(auctionTiming, columns) {
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
    private JPanel getButtonContainer() {
        JPanel toSend = new JPanel(new GridLayout(3,1));
        JButton verifyDates = new JButton("Verify");
        JButton showAuctions = new JButton("Show Filtered Auctions");
        JButton back = new JButton("Back");
        showAuctions.setEnabled(false);

        verifyDates.addActionListener((ActionEvent e) -> {
            this.verifyDates(showAuctions);
        });
        showAuctions.addActionListener((ActionEvent e) -> {
            try {
                this.auctionInFrame = this.dataControl.getAuctionsWithBounds(this.startTime, this.endTime);
            } catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
            this.auctionsInTimeFramePanel.removeAll();
            this.setElements();
            this.auctionsInTimeFramePanel.repaint();
            this.auctionsInTimeFramePanel.revalidate();
            });
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(5); // 5 - revert to main menu
        });
        toSend.add(verifyDates);
        toSend.add(showAuctions);
        toSend.add(back);
        return toSend;
    }

    /**
     *
     * @param showAuctionBtn
     */
    private void verifyDates(JButton showAuctionBtn) {
        boolean isErrorShown = false;
        try {
            this.startTime = this.getDateFromText(this.start.getText());
        } catch (DateTimeException err) {
            isErrorShown = true;
            showErrorDialog("Error in parsing start date input, please try again.");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException err) {
            isErrorShown = true;
            showErrorDialog("Please enter a valid start date.");
        }
        try{
            this.endTime = this.getDateFromText(this.end.getText());
        }
        catch (DateTimeException err) {
            isErrorShown = true;
            showErrorDialog("Error in parsing end date input, please try again.");
        } catch (NumberFormatException |ArrayIndexOutOfBoundsException err) {
            isErrorShown = true;
            showErrorDialog("Please enter a valid end date.");
        }

        if (!isErrorShown) {
            if (this.startTime.equals(this.endTime)) {
                showErrorDialog("Start and End times are same, there must be an offset.");
            } else if (this.endTime.isBefore(this.startTime)) {
                showErrorDialog("Start date is after End date.");
            } else {
                showAuctionBtn.setEnabled(true);
                JOptionPane.showMessageDialog(auctionsInTimeFramePanel,
                        "Your Entered Dates are valid! Click the Show Filtered Auctions Button");
            }
        }
    }

    /**
     *
     * @param message
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(auctionsInTimeFramePanel, message, "Error", JOptionPane.ERROR_MESSAGE);
        /*JOptionPane.showMessageDialog(maxAuctionScreen, "Value changed from " + prevVal +
                " to " + possibleFutureValue);*/
    }

    /**
     *
     * @param time
     * @return
     * @throws DateTimeException
     */
    private LocalDateTime getDateFromText(String time) throws DateTimeException {
        String[] parts = time.split(" ");
        String[] temp = parts[0].split("/");
        int[] monthDayYear = new int[3];
        for(int i = 0; i < temp.length; i++) {
            monthDayYear[i] = Integer.parseInt(temp[i]);
        }
        String[] timeTemp = parts[1].split(":");
        int[] hourMinute = new int[timeTemp.length];
        for(int i = 0; i < timeTemp.length; i++) {
            Integer integer = Integer.valueOf(timeTemp[i]);
            hourMinute[i] = integer.intValue();
        }
        int hour = get24Hour(hourMinute[0], parts[2]);
        LocalDateTime result = LocalDateTime.of(monthDayYear[2], monthDayYear[0],
                monthDayYear[1], hour, hourMinute[1]);
        System.out.println(result);
        return result;
    }

    /**
     *
     * @param i
     * @param s
     * @return
     */
    public int get24Hour(int i, String s) {
        int hour = 0;
        if(s.contains("P") && i <= 12) {
            hour = i + 12;
        } else {
            hour = i;
        }
        return hour;
    }

    /**
     *
     * @return
     */
    private JPanel getInstructions () {
        JPanel toSend = new JPanel(new GridLayout(4, 1));
        String indent = "\t\t\t\t\t\t";
        JLabel title = new JLabel("\tInstructions:");
        JLabel condition1 = new JLabel(indent + "1) Enter start and end timings (inclusive)");
        JLabel condition2 = new JLabel(indent + "2) Verify");
        JLabel condition3 = new JLabel(indent + "3) Show Filtered Auctions");

        toSend.add(title);
        toSend.add(condition1);
        toSend.add(condition2);
        toSend.add(condition3);
        return toSend;
    }

    /**
     *
     * @return
     */
    public JPanel getAuctionsInTimeFramePanel() {
        return this.auctionsInTimeFramePanel;
    }
}
