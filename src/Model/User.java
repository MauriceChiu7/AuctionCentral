import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/** Represents a Auction Central User.
 * @author Hari G Kuduva
 * @version May 29, 2018
 */
public class User implements Serializable {
    /** User's name **/
	protected String name;
	/** User's email **/
	protected String email;
	/** User's username **/
    protected String username;
    /** User's address **/
    protected String address;
    /** User's phone number. **/
    protected String phoneNumber;
    /** Contains all the auction that the bidder has placed bids in. */
	protected HashMap<String, Auction> auctions = new HashMap<String, Auction>();
	


    /** Creates a user with 4 parameters.
     * @param email User's email
     * @param username User's username
     * @param address User's address
     * @param phoneNumber User's phoneNumber */
    public User(final String email, final String username,
            final String address, final String phoneNumber) {
         this.email = email;
         this.username = username;
         this.address = address;
         this.phoneNumber = phoneNumber;
    }

}
