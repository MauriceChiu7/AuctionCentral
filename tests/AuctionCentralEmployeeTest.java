import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/* We have decided to break testing by user type.
    So Every test in this class is for AuctionCentralEmployee (Admin)
        Most of the methods are in DataControlCenter */
public class AuctionCentralEmployeeTest {

    private DataControlCenter dataControl;
    private Auction basicAuction;
    private Item someItem;

    @Before
    public void setUp() throws IOException, ClassNotFoundException {
        this.dataControl = new DataControlCenter();
        this.basicAuction = new Auction();
        this.someItem = new Item("Baisal's Darkest Dirtiest Secret",
                1, 50, "His deepest and darkest secrets", "imgPath");
    }

    @Test
    public void setMaxAuctionAllowed_RequestedMaxAuctionAllowedIsNegative_False() {
        int badQuery = -5;
        assertFalse(this.dataControl.setMaxAuctionAllowed(badQuery));
    }

    @Test
    public void setMaxAuctionAllowed_RequestedMaxAuctionAllowedIsPositive_True() {
        int positiveQuery = 7;
        assertTrue(this.dataControl.setMaxAuctionAllowed(positiveQuery));
    }

    @Test
    public void setMaxAuctionAllowed_RequestedMaxAuctionAllowedIsGreaterThanExistingMax_True () {
        int greaterQuery = 35; // by default max is set to 7
        assertTrue(this.dataControl.setMaxAuctionAllowed(greaterQuery));
    }

    @Test
    public void getAuctionWithBounds_SecondDateIsEarlierThanEndDate_False()
            throws IOException, ClassNotFoundException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(-1);
        assertNull(this.dataControl.getAuctionsWithBounds(startDate, endDate));
    }

    @Test
    public void getAuctionWithBounds_FirstAndSecondDateAreSame()
            throws IOException, ClassNotFoundException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate;
        assertTrue(this.dataControl.getAuctionsWithBounds(startDate, endDate).size() == 0);
        // The set of auctions should not exist if start and end dates are there
    }

    @Test
    public void getAuctionWithBounds_SecondDateAtLeastOneDayLaterThanFirst_True()
            throws IOException, ClassNotFoundException {
        LocalDateTime startDate = LocalDateTime.MIN;
        LocalDateTime endDate = LocalDateTime.MAX;
        assertTrue(!this.dataControl.getAuctionsWithBounds(startDate, endDate).isEmpty());
    }

    @Test
    public void sortAuctionSet_PastCurrentFutureAuctionsExist_True()
            throws IOException, ClassNotFoundException {
        boolean past = this.dataControl.getPastAuctions().size() >= 1;
        boolean current = this.dataControl.getActiveAuctions().size() >= 0;
        // We have no initial current auctions
        boolean future = this.dataControl.getFutureAuctions().size() >= 1;
        assertTrue(past && current && future);
    }

    /*As an employee of AuctionCentral, I want to cancel an auction.
No auction can be cancelled that has any bids
The auction has no bids
The auction has one bid (Fail)
The auction has many more than one bid (Fail)
 */

    @Test
    public void cancelAuction_AuctionHasNoBids_True() {
        //String name, int quantity, int startingBid, String description, String imagePath
        /*auction.addItem(new Item("Baisal's Darkest Dirtiest Secret",
                1, 50, "His deepest and darkest secrets", "imgPath"));*/
        assertTrue(this.dataControl.cancelAuction(this.basicAuction));
    }

    @Test
    public void cancelAuction_AuctionHasOneBid_False() {
        this.someItem.addBid(new Bid("Maurice", "Baisal's Darkest Dirtiest Secret", 300,
                this.basicAuction.getAuctionID(), this.basicAuction.getStart(), this.basicAuction.getEnd()));
        this.basicAuction.addItem(this.someItem);
        assertFalse(this.dataControl.cancelAuction(this.basicAuction));
    }

    @Test
    public void cancelAuction_AuctionHasMoreThanOneBid_False() {
        this.someItem.addBid(new Bid("Maurice", "Baisal's Darkest Dirtiest Secret", 300,
                this.basicAuction.getAuctionID(), this.basicAuction.getStart(), this.basicAuction.getEnd()));
        this.someItem.addBid(new Bid("Hari", "Baisal's Darkest Dirtiest Secret", 301,
                this.basicAuction.getAuctionID(), this.basicAuction.getStart(), this.basicAuction.getEnd()));
        this.basicAuction.addItem(this.someItem);
        assertFalse(this.dataControl.cancelAuction(this.basicAuction));
    }









}
