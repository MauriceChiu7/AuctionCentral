import javax.swing.*;
import java.awt.TextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;


public class AddItemScreen {
    private static final int MAX_ITEMS_PER_AUCTION = 10;
    private Auction auction;
    private JFrame addItemFrame;

    public AddItemScreen(Auction theAuction) {
        addItemFrame = new JFrame("Add Item");
        this.auction = theAuction;
        addItemFrame.setLayout(new BorderLayout());
        addItemFrame.add(new JLabel("Add Your Item"));
        addItemFrame.setLocationRelativeTo(null);
        addItemFrame.setSize(400, 300);

        checkItemMax();
    }

    private void checkItemMax() {
        if(auction.getItems().size() < MAX_ITEMS_PER_AUCTION) {
           addItem();
        } else {
            JOptionPane.showMessageDialog(addItemFrame, "You have added the maximum number of items to this auction.");
            addItemFrame.dispose();
        }
    }

    private void addItem() {
//        Item Name: ItemName
//        Description: Description
//        Quantity: Quantity
//        Starting Price: $Starting Price

        JLabel itemName = new JLabel("Item Name: ");
        JLabel description = new JLabel("Description: ");
        JLabel quantity = new JLabel("Quantity (number of items in lot): ");
        JLabel startingPrice = new JLabel("Starting Bid Amount: ");

        TextField nameField = new TextField("", 20);
        TextField descriptionField = new TextField("", 20);
        TextField quantityField = new TextField("", 20);
        TextField startingField = new TextField("", 20);

        JPanel itemPanel = new JPanel(new GridLayout(0, 2));

        itemPanel.add(itemName);
        itemPanel.add(nameField);

        itemPanel.add(description);
        itemPanel.add(descriptionField);

        itemPanel.add(quantity);
        itemPanel.add(quantityField);
        
        itemPanel.add(startingPrice);
        itemPanel.add(startingField);

        addItemFrame.add(itemPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Item");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                int quantityGiven = Integer.parseInt(quantityField.getText());
                int bid = Integer.parseInt(startingField.getText());
                String desc = descriptionField.getText();
                String path = "";

                Item newItem = new Item(name, quantityGiven, bid, desc, path);

                auction.addItem(newItem);

                JOptionPane.showMessageDialog(addItemFrame, "Item Added");

                nameField.setText("");
                quantityField.setText("");
                startingField.setText("");
                descriptionField.setText("");

                checkItemMax();
            }
        });

        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItemFrame.dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);
        addItemFrame.add(buttonPanel, BorderLayout.SOUTH);
        addItemFrame.setVisible(true);
    }
}
