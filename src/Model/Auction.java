import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.io.Serializable;
import java.util.*;

/** Meant to represent an auction with its entities */

/**
 * @author Hari G Kuduva
 * @author Shannon Weston
 * @version May 29, 2018
 */
public class Auction implements Serializable {
	private static final int MIN_SCHEDULE_OUT_DAYS = 14;
	
    /** Organization's name **/
    private String organization;
    /** List of items that the auction has **/
    protected ArrayList<Item> items;
    /** Start time of the auction **/
    private LocalDateTime start;
    /** End time of the auction **/
    private LocalDateTime end;
    /** Online start time of the auction **/
    private LocalDateTime onlineStart;
    /** Auction Identification number **/
    private int auctionID;

    private boolean isCanceled;

    //Shannon Weston
    /** Creation date for the auction **/
    private LocalDateTime creationDate;

    /** Creates an auction with 3 parameters
     * @param organization the organization name
     * @param startClock the start time
     * @param endClock the end time
     */
    public Auction (String organization, LocalDateTime startClock, LocalDateTime endClock) {
        this.items = new ArrayList<Item>();
        this.onlineStart = startClock.minusDays(MIN_SCHEDULE_OUT_DAYS);
        this.isCanceled = false;
    }

    /**
     *
     * @param isCanceled
     */
    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    /**
     *
     * @return
     */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * Creates an auction with 4 parameters
     * @param organization the organization name
     * @param startClock the start time
     * @param endClock the end time
     * @param auctionID the auction identification number
     */
    public Auction(String organization, LocalDateTime startClock, LocalDateTime endClock, int auctionID) {
        this.organization = organization;
        this.start = startClock;
        this.end = endClock;
        this.items = new ArrayList<Item>();
        this.creationDate = LocalDateTime.now();
        this.auctionID = auctionID;
        //fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        onlineStart = this.start.minusDays(MIN_SCHEDULE_OUT_DAYS);
        this.isCanceled = false;
    }

    /** Creates an auction with 1 parameter
     * @param organization the organization name */
    public Auction (String organization) {
        this.organization = organization;
        this.creationDate = LocalDateTime.now();
        this.items = new ArrayList<Item>();
        //fmt = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        onlineStart = this.start.minusDays(MIN_SCHEDULE_OUT_DAYS);
    }

    /** Creates an empty auction with 0 parameters */
    //(String organization, LocalDateTime startClock, LocalDateTime endClock, int auctionID)
    public Auction() {
        //Empty Auction for BidderConsole.java
    	//this.items = new ArrayList<Item>();
    	//this.creationDate = LocalDateTime.now();
    	//this.isCanceled = false;
        this("", LocalDateTime.now(), LocalDateTime.now().plusDays(1), -1);
    	//onlineStart = start.minusDays(MIN_SCHEDULE_OUT_DAYS);
    }

    /**
     *
     * @param other
     */
    public Auction(Auction other) {
        this.organization = other.organization;
        this.auctionID = other.auctionID;
        this.items = new ArrayList<>();
        this.start = other.start;
        this.end = other.end;
    }

    @Override
    public boolean equals(Object obj) {
        return this.auctionID == ((Auction) obj).auctionID;
    }

    @Override
    public int hashCode() {
        return this.auctionID;
    }

    /**
     *
     * @param organization
     */
    public void setOrganization(String organization) {
    	this.organization = organization;
    }

    /**
     *
     * @return
     */
    public String getOrganization() {
    	return this.organization;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     *
     * @param date
     */
    public void setStart(LocalDateTime date) {
        this.start = date;
    }

    /**
     *
     * @param date
     */
    public void setEnd(LocalDateTime date) {
        this.end = date;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getEnd() {
    	return this.end;
    }

    /**
     *
     * @param date
     */
    public void setOnlineStart(LocalDateTime date) {
    	this.onlineStart = date;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getOnlineStart() {
    	return onlineStart;
    }

    /**
     *
     * @param item
     */
    public void addItem(Item item) {
        this.items.add(item);
    }

    /**
     *
     * @return
     */
    public ArrayList<Item> getItems() {
    	return this.items;
    }

    /**
     *
     * @return
     */
    public int getAuctionID() {
        return this.auctionID;
    }

    /**
     *
     * @param nextAvailableAuctionId
     */
    public void setAuctionId(int nextAvailableAuctionId) {
        this.auctionID = nextAvailableAuctionId;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Auction by " + this.organization + " has " + this.items.size() + " items.\n");
    	sb.append("\tStart Time : " + this.start.toString());
    	sb.append("\n\tEnd Time : " + this.end.toString());
    	sb.append("\n\tOnline Launch: " + this.onlineStart.toString());
    	sb.append("\n\n");
    	return sb.toString();
    }
    
}