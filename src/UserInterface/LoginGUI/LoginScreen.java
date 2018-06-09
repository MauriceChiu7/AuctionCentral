import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.TextField;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;



public class LoginScreen extends Observable {
//	private JLabel userNameInput = new JLabel("Username: ");
//	private JFrame frame = new JFrame("Parent");
//	private String message = "Please input your username: ";
	private JFrame loginFrame;
	private ArrayList<String> userTypes = new ArrayList<String>();
	private TextField userField;
	private ButtonGroup userGroup;
	private String userName;
	private String userType;

	public LoginScreen() {
		loginFrame = new JFrame("Welcome to Auction Central");
    	userTypes.add("Auction Central Employee");
    	userTypes.add("Non-Profit Contact");
    	userTypes.add("Bidder");
		this.userField = new TextField("", 20);
		this.userGroup = new ButtonGroup();
		this.userName = "";
		this.userType = "";

		loginFrame.add(createLoginPanel(), BorderLayout.CENTER);
		loginFrame.add(createGoButton(), BorderLayout.SOUTH);

		loginFrame.setLocationRelativeTo(null);
		Dimension max = new Dimension(400, 250);
		loginFrame.setSize(max);
		loginFrame.setMaximumSize(max);
	}

	public void start() {
		loginFrame.setVisible(true);
	}

	private JPanel createLoginPanel() {
		JPanel login = new JPanel(new GridLayout(0,2));

		JLabel typeLabel = new JLabel("Please Select Your User Type: ");
		JLabel userLabel = new JLabel("Please Enter Your Username: ");

		login.add(typeLabel);
		login.add(createButtonPanel());
		login.add(userLabel);
		login.add(userField);

		return login;
	}


	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

//		for(int i = 0; i < userTypes.size(); i++) {
//			String s = userTypes.get(i);
//			JRadioButtonMenuItem button = new JRadioButtonMenuItem(new AbstractAction() {
//				public void actionPerformed(ActionEvent e) {
//					userType = s;
//				}
//			});
//			this.userGroup.add(button);
//			buttonPanel.add(button);
//		}

		JToggleButton ac = new JToggleButton(userTypes.get(0));
		JToggleButton np = new JToggleButton(userTypes.get(1));
		JToggleButton bidder = new JToggleButton(userTypes.get(2));

		ac.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userType = userTypes.get(0);
			}
		});

		np.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userType = userTypes.get(1);
			}
		});

		bidder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userType = userTypes.get(2);
			}
		});

		userGroup.add(ac);
		userGroup.add(np);
		userGroup.add(bidder);

		buttonPanel.add(ac);
		buttonPanel.add(np);
		buttonPanel.add(bidder);

		return buttonPanel;
	}

	private JButton createGoButton() {
		JButton goButton = new JButton("Go");

		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userName = userField.getText();
				//userType = userGroup.getSelection().toString();
				getUser();
//				System.out.println(userName);
//				System.out.println(userType);
			}
		});

		return goButton;
	}

	public void getUser() {
		ArrayList<String> user = new ArrayList<String>();
		user.add(userType);
		user.add(userName);
		notifyObservers(user);
	}
}
