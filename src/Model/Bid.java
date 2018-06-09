import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/** Represents a bid.
 * @author Hari G Kuduva
 * @version May 29, 2018
 */
public class Bid implements Serializable {
    /** Name of the bidder. **/
    private String bidderName;
    /** Name of the item. **/
    private String itemName;
    /** Bid amount. **/
    private int amount;

    private LocalDateTime start;

    private LocalDateTime end;

    private int auctionId;



    /** Creates a bid with 3 parameters
     * @param bidderName the bidder's name
     * @param itemName the item's name
     * @param amount bid amount */
    public Bid(String bidderName, String itemName, int amount, int auctionId,
               LocalDateTime start, LocalDateTime end) {
        this.bidderName = bidderName;
        this.itemName = itemName;
        this.amount = amount;
        this.auctionId = auctionId;

        this.start = start;
        this.end = end;
    }

    /**
     *
     * @param bidderName
     * @param itemName
     * @param amount
     */
    public Bid(String bidderName, String itemName, int amount) {
        this(bidderName, itemName, amount, -1, LocalDateTime.now(), LocalDateTime.now());
    }

    /**
     *
     * @return
     */
    public String getBidder(){
        return this.bidderName;
    }

    /**
     *
     * @return
     */
    public String getItem() {
        return this.itemName;
    }

    /**
     *
     * @return
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     *
     * @return
     */
    public int getAuctionID() { return this.auctionId; }

    /**
     *
     * @return
     */
    public LocalDateTime getStart() { return this.start; }

    /**
     *
     * @return
     */
    public LocalDateTime getEnd() { return this.end; }

    @Override
    public boolean equals(Object obj) {
        return this.itemName == ((Bid) obj).itemName;
    }

    @Override
    public int hashCode() {
        return this.amount;
    }
}
