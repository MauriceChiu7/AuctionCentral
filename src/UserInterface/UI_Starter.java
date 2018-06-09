import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** UI Kickstart of the program.
 * BIN FILES MUST EXIST BEFORE EXECUTION!
 * @author Hari Kuduva
 */
public class UI_Starter {
    /** Main Data Control. **/
    private static DataControlCenter dataControl;
    //private static LoginScreen loginScreen;

    /** Private constructor to prevent instantiation.*/
    private UI_Starter() { }

    public static void main(String... args) throws IOException, ClassNotFoundException {
        dataControl = new DataControlCenter();
        //loginScreen = new LoginScreen();
        //SerializeData.main();
        login();
    }
    
    private static void login() throws ClassNotFoundException, IOException{
    	//loginScreen.addObserver(this);
		//loginScreen.start();

		//JOptionPane Login Code
    	JFrame frame = new JFrame("Auction Central");
    	
    	String[] userTypeOptions = {"Bidder", "Non-Profit Contact", "Auction Central Employee"};

    	Object userTypeInput = JOptionPane.showInputDialog(frame, "What type of user are you?", "Choose User Type", JOptionPane.CANCEL_OPTION, null, userTypeOptions, "None");
    	
    	String userType = (String) userTypeInput;

    	String username;
    	username = (String) JOptionPane.showInputDialog(frame, "Please Enter Your Username: ", "Login to Auction Central", JOptionPane.OK_CANCEL_OPTION);
    	verifyAndLaunchSpecificUser(userType, username);
		//end JOptionPane login
    }
    
    private static void verifyAndLaunchSpecificUser(String usertype, String username)
			throws IOException, ClassNotFoundException {
    	switch (usertype) {
    	case "Bidder":
    		if(dataControl.isBidderValid(username)) {
    			new BidderFrame(dataControl.getBidderByUsername(username), dataControl);
    		}
    	case "Non-Profit Contact":
    		if(dataControl.isNonProfitValid(username)) {
				new NPContactFrame(dataControl.getNPContactByUsername(username), dataControl);
    		}
    	case "Auction Central Employee":
    		if (dataControl.isAdminValid(username)) {
    			new AuctionCentralEmployeeFrame(dataControl.getAdminByUsername(username), dataControl);
			}
    	}
    }
}
