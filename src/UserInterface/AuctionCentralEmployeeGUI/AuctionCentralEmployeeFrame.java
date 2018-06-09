import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Baisal
 * @version May 29, 2018
 */
public class AuctionCentralEmployeeFrame implements Observer {

    private AuctionCentralEmployee currAdmin;
    private DataControlCenter dataControl;
    private JFrame frame;
    private MainScreen_Admin main;
    private ChangeMaxAuctionScreen changeMaxAuctionScreen;
    private AuctionsInChronoOrderScreen chronoScreen;
    private CancelAuctionScreen cancelAuctionScreen;
    private AuctionsInTimeFrameScreen timeFrameScreen;


    /**
     *
     * @param currAdmin
     * @param dataControl
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AuctionCentralEmployeeFrame(AuctionCentralEmployee currAdmin, DataControlCenter dataControl) throws IOException, ClassNotFoundException {
        this.currAdmin = currAdmin;
        this.dataControl = dataControl;
        this.frame = new JFrame();
        this.frame.setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Auction Central Employee - " + currAdmin.getName());

        this.main = new MainScreen_Admin(currAdmin, dataControl);
        this.main.addObserver(this);

        this.changeMaxAuctionScreen = new ChangeMaxAuctionScreen(this.dataControl);
        this.changeMaxAuctionScreen.addObserver(this);

        this.chronoScreen = new AuctionsInChronoOrderScreen(dataControl);
        this.chronoScreen.addObserver(this);

        this.cancelAuctionScreen = new CancelAuctionScreen(dataControl);
        this.cancelAuctionScreen.addObserver(this);

        this.timeFrameScreen = new AuctionsInTimeFrameScreen(dataControl);
        this.timeFrameScreen.addObserver(this);


        this.frame.setSize(400,500);
        this.frame.add(main.getMainScreen(), BorderLayout.CENTER);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }


    @Override
    public void update(Observable parent, Object arg) {
        int result = (Integer) arg;
        if (result == 1) {
            System.out.println("First button");
            this.frame.getContentPane().removeAll();
            this.frame.add(this.changeMaxAuctionScreen.getMaxAuctionScreen(), BorderLayout.CENTER);
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == 2) {
            System.out.println("Second button");
            this.frame.getContentPane().removeAll();
            this.frame.add(this.timeFrameScreen.getAuctionsInTimeFramePanel(), BorderLayout.CENTER);
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == 3) {
            System.out.println("Third button");
            this.frame.getContentPane().removeAll();
            try {
                this.frame.add(this.chronoScreen.getChronoOrderScreen(), BorderLayout.CENTER);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == 4) {
            System.out.println("Fourth button");
            this.frame.getContentPane().removeAll();
            this.frame.add(this.cancelAuctionScreen.getCancelAuctionScreen(), BorderLayout.CENTER);
            this.frame.repaint();
            this.frame.revalidate();
        } else if (result == 5) {
            System.out.println("Back button");
            this.frame.getContentPane().removeAll();
            this.frame.add(this.main.getMainScreen(), BorderLayout.CENTER);
            this.frame.repaint();
            this.frame.revalidate();
        }
    }
}
