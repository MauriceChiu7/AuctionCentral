import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/** Represents an item in an auction.
 * @author Hari G Kuduva
 * @version May 29, 2018
 */
public class Item implements Serializable {
    //Group
    /** Item's name **/
    private String name;
    /** Item's quantity **/
    private int quantity;
    /** Item's description **/
    private String  description;
    /** Item's starting bid **/
    private int startingBid;
    /** Item's imagePath **/
    private String imagePath;
    /** Item's current list of bids **/
    private ArrayList<Bid> bids;

    private int highestBid;

    private Bid bidWithHighestBid;

    private static final long serialVersionUID = 1;

    public Item(){
        this("", 0, 0, "", "");
    }

    /** Creates an Item with 5 parameters
     * @param name  Item's name
     * @param quantity Item's quantity
     * @param startingBid Item's starting bid
     * @param description Item's description
     * @param imagePath Item's image path */
    public Item(String name, int quantity, int startingBid, String description, String imagePath) {
        this.name = name;
        this.quantity = quantity;
        this.startingBid = startingBid;
        this.description = description;
        this.imagePath = imagePath;
        this.bids = new ArrayList<>();
        this.highestBid = startingBid;
        this.bidWithHighestBid = new Bid("", "", 0, 0, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    }

    /**
     *
     * @return
     */
    public Bid getBidWithHighestBid(){
        return this.bidWithHighestBid;
    }

    /**
     *
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     *
     * @param qty
     */
    public void setQuantity(int qty) {
        this.quantity = qty;
    }

    /**
     *
     * @param startingBid
     */
    public void setStartingBid(int startingBid){
        this.startingBid = startingBid;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param imagePath
     */
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "Item name: " + this.name + ", Quantity: " + this.quantity + ", StartingBid: " + this.startingBid +
                ", Description: " + this.description;
    }

    //Group

    /**
     *
     * @param bid
     */
    public void addBid(Bid bid) {
        bids.add(bid);
        this.bidWithHighestBid = highestBid();
        this.highestBid = this.getCurrentBid();

    }

    /**
     *
     * @return
     */
    public Bid highestBid() {
        Bid toSend = bids.get(0);
        for (int i = 0; i < bids.size(); i++) {
            if (bids.get(i).getAmount() > toSend.getAmount()) {
                toSend = bids.get(i);
            }
        }
        return toSend;
    }

    //Group

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public int getQuantity() {
    		return this.quantity;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
    		return this.description;
    }

    /**
     *
     * @return
     */
    public ArrayList<Bid> getBids() {
    	return this.bids;
    }

    /**
     *
     * @return
     */
    public String getImagePath() {return this.imagePath; }

    /**
     *
     * @return
     */
    public int getCurrentBid() {
        int bid = 0;
        
        if(bids.size() == 0) {
            //There are no bids yet so startingBid is bid to beat.
            bid = startingBid;
        } else {
            //Bids will be added to the list in ascending order.
            //Therefore, last value in bids will be the bid to beat.
            //To make this secure, bids must be immutable.
            //bid = bids.get(bids.size() - 1).getAmount();
            for (Bid b : this.bids) {
                if (b.getAmount() > bid) {
                    bid = b.getAmount();
                    Arrays.toString(this.bids.toArray());
                }
            }
        }
        
        return bid;
    }
}


