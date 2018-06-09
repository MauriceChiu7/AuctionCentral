import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Observable;

public class SubmitAuctionRequest_Screen extends Observable implements ActionListener {

    private JPanel viewAllAuctions;
    private DataControlCenter dataControl;
    private NPContact currContact;

    //These values need to be adjusted per company policy.
    private static final int MAX_SCHEDULE_OUT_DAYS = 60;
    private static final int MIN_SCHEDULE_OUT_DAYS = 14;

    private LocalDate farthestDate;
    private LocalDate nearestDate;
    private DateTimeFormatter fmt;
    private LocalDateTime inputDate;
    private LocalDateTime endDate;

    private JButton button;
    private JTextField text;

   // private boolean valid;
    private LocalDateTime dateTime;

    /**
     *
     * @param dcc
     * @param currContact
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public SubmitAuctionRequest_Screen (DataControlCenter dcc, NPContact currContact) throws IOException, ClassNotFoundException {

        this.viewAllAuctions = new JPanel(new BorderLayout());
        this.dataControl = dcc;
        this.currContact = currContact;
        farthestDate = LocalDate.now().plusDays(MAX_SCHEDULE_OUT_DAYS);
        nearestDate = LocalDate.now().plusDays(MIN_SCHEDULE_OUT_DAYS);
        fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        inputDate = LocalDateTime.now();
        endDate = LocalDateTime.now();

        this.setElements();
    }


    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public JPanel getSubmitAuctionRequest_Screen() throws IOException, ClassNotFoundException {
        this.viewAllAuctions.removeAll();
        this.setElements();
        return this.viewAllAuctions;
    }


    private void setElements() throws IOException, ClassNotFoundException {


        JPanel container = new JPanel();
        this.viewAllAuctions.add(container, BorderLayout.CENTER);


        JPanel accountDetails = this.getAccountDetails();
        container.add(accountDetails, BorderLayout.NORTH);

        JPanel requestDate = this.requestDate();
        container.add(requestDate, BorderLayout.CENTER);


        /** Back Button Logic*/
        JButton back = new JButton("Back");
        this.viewAllAuctions.add(back, BorderLayout.SOUTH);
        back.addActionListener((ActionEvent e) -> {
            setChanged();
            notifyObservers(5);
        });
    }


    /**
     * Gives the user the welcome screen and the date which they can request an auction
     */
    private JPanel getAccountDetails() {
        JPanel toSend = new JPanel(new GridLayout(5, 2, 20, 20));
        String farthestDateFormatted = farthestDate.format(fmt);
        String nearestDateFormatted = nearestDate.format(fmt);

        String indent = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        JLabel welcomeForm = new JLabel(indent + "\t   Welcome to the Auction Creation Form! ");

        String indent1 = "\t\t\t\t\t\t";
        JLabel title = new JLabel(indent1 +  "\t We are currently accepting auctions from "+ nearestDateFormatted + " up to "
                + farthestDateFormatted + ". \n");

        JLabel name = new JLabel(indent1 + "\t Enter your date as follows or go back: MM/DD/YYYY HH:MM AM/PM (ex: 5/6/2018 3:44 PM) \t");
        toSend.add(welcomeForm);
        toSend.add(title);
        toSend.add(name);
        return toSend;
    }


    private JPanel requestDate() {
        JPanel toSend = new JPanel( new FlowLayout() );
        this.text = new JTextField(20);
        this.button = new JButton("Submit");

        toSend.add(new JLabel("Requested Date "));
        toSend.add(text);

        button.addActionListener(this);
        toSend.add(button);

        return toSend;
    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if(e.getSource() == button){

            String get = text.getText().toString();
            String[] parts = get.split(" ");
            this.dateTime = formatTime(parts);

            try {
                if(checkAuction()){
                    confirmAuctionDateTime();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    /** Formats the given time
     * @param parts the time data to be formatted */
    public LocalDateTime formatTime(String[] parts) {
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

        inputDate = LocalDateTime.of(monthDayYear[2], monthDayYear[0], monthDayYear[1], hour, hourMinute[1]);
        return inputDate;
    }


    /**Converts time given in 12 hour time to 24 hour time.*/
    public int get24Hour(int i, String s) {
        int hour = 0;
        if(s.contains("P") && i <= 12) {
            hour = i + 12;
        } else {
            hour = i;
        }
        return hour;
    }

    /** Checks the auction to see if it is available for scheduling.
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk */
    public boolean checkAuction() throws IOException, ClassNotFoundException {

        boolean valid = dataControl.isRequestedAuctionDateValid(dateTime);

        if(valid) {
            boolean available = dataControl.isRequestedAuctionDateAvailable(inputDate, endDate);
            if(!available) {
                JOptionPane.showMessageDialog(null, "Your date or time is unavailable. Please try again.");
                return false;
            } else {
                return valid;
            }
        } else {
                JOptionPane.showMessageDialog(null, "Your requested date is too soon or too far away. Please try again");
                return false;
        }

    }

    /** Displays confirmation to the user that their auction has been scheduled.
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk */
    public void confirmAuctionDateTime() throws IOException, ClassNotFoundException {
        String inputFormatted = inputDate.format(fmt);
        JOptionPane.showMessageDialog(null, "Your auction is scheduled for: " +
                inputFormatted.toString() + ". Thank you! ");
        createAuction();
    }

    /** Creates the auction with the info gathered from the user. **/
    public void createAuction() {


        LocalDateTime startOnline = LocalDateTime.now().plusDays(MIN_SCHEDULE_OUT_DAYS);
        LocalDateTime start = inputDate;
        LocalDateTime end = endDate;

        Auction auction = new Auction();

        auction.setStart(start);
        auction.setEnd(end);
        auction.setOnlineStart(startOnline);

        auction.setOrganization(currContact.getName());
        auction.setAuctionId(this.dataControl.getNextAvailableAuctionId());

        auction.setStart(start);
        auction.setOnlineStart(startOnline);
        auction.setEnd(end);

        dataControl.addAuction(auction);

        new AddItemScreen(auction);
    }
}
