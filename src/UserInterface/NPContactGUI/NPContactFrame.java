import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class NPContactFrame implements Observer{


    private JFrame frame;
    private DataControlCenter dataControl;
    private NPContact currContact;
    private NPContact_MainScreen main;
    private SubmitAuctionRequest_Screen submitAuctionRequestScreen;
    private ViewSubmittedAuction_Screen viewSubmittedAuctionScreen;


    /**
     *
     * @param currContact
     * @param dataControl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public NPContactFrame(NPContact currContact, DataControlCenter dataControl) throws IOException, ClassNotFoundException {

        this.dataControl = dataControl;
        this.currContact = currContact;

        this.frame = new JFrame();
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle(String.format("Welcome %s! You have been logged in as a Non-Profit Contact.", this.currContact.getName()));


        this.main = new NPContact_MainScreen(currContact, dataControl);
        this.main.addObserver(this);

        this.submitAuctionRequestScreen = new SubmitAuctionRequest_Screen(dataControl, currContact);
        this.submitAuctionRequestScreen.addObserver(this);

        this.viewSubmittedAuctionScreen = new ViewSubmittedAuction_Screen(dataControl, currContact);
        this.viewSubmittedAuctionScreen.addObserver(this);

        this.frame.setSize(600,500);
        this.frame.add(main.getMainScreen(), BorderLayout.CENTER);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

    }


    @Override
    public void update(Observable o, Object arg) {

        int result = (Integer) arg;
        if (result == 1) {
            this.frame.getContentPane().removeAll();
            try {
                this.frame.add(this.viewSubmittedAuctionScreen.getViewAllAuctionsScreen(), BorderLayout.CENTER);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.frame.repaint();
            this.frame.revalidate();

        } else if (result == 2) {
            this.frame.getContentPane().removeAll();
            try {
                this.frame.add(this.submitAuctionRequestScreen.getSubmitAuctionRequest_Screen(), BorderLayout.CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.frame.repaint();
            this.frame.revalidate();

            /**Code for Back Button*/
        } else if (result == 5) {
            this.frame.getContentPane().removeAll();
            this.frame.add(this.main.getMainScreen(), BorderLayout.CENTER);
            this.frame.repaint();
            this.frame.revalidate();
        }

    }
}

