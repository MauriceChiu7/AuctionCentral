import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BidderTest {

    private DataControlCenter dataControl;
    private Bidder normalBidder;
    private Auction past;
    private Auction current;
    private Auction future;
    private Item someItem;
    private Bid someBid;

    @Before
    public void setUp() throws IOException, ClassNotFoundException {
        this.dataControl = new DataControlCenter();
        this.normalBidder = new Bidder("Baisal", "Baisal@baisal.baisal", "baisal",
                "123 Elm Street", "1234567891", 10000);
        this.someItem = new Item("Baisal's Darkest Dirtiest Secret",
                1, 50, "His deepest and darkest secrets", "imgPath");
        this.past = new Auction();
        this.current = new Auction();
        this.future = new Auction();
        this.someBid = new Bid("Maurice", "Baisal's Darkest Dirtiest Secret",
                100, -1, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    }

    @Test
    public void getAuctionsCurrBidderCanBidOn_PastAuctionExists_False()
            throws IOException, ClassNotFoundException {
        HashSet<Auction> auctions = this.dataControl.getAuctionsCurrBidderCanBidOn(this.normalBidder);
        boolean containsPastAuction = false;
        for (Auction auction : auctions) {
            if (auction.getEnd().isBefore(LocalDateTime.now())) {
                containsPastAuction = true;
                break;
            }
        }
        assertFalse(containsPastAuction);
    }

    @Test
    public void getAuctionCurrBidderCanBidOn_ExistsFutureAuctionICanBidOn_True()
            throws IOException, ClassNotFoundException {
        HashSet<Auction> auctions = this.dataControl.getAuctionsCurrBidderCanBidOn(this.normalBidder);
        boolean containsFutureAuction = false;
        for (Auction auction : auctions) {
            if (auction.getStart().isAfter(LocalDateTime.now())) {
                containsFutureAuction = true;
                break;
            }
        }
        assertTrue(containsFutureAuction);
    }

    @Test
    public void allItemTest_AuctionDateIsPast_True() {
        /* We had no specific method (method name) that did this behavior, we always did on fly */
        this.past.addItem(this.someItem);
        assertTrue(this.past.items.size() > 0);
    }

    @Test
    public void allItemTest_AuctionDateIsCurrent_True() {
        this.current.addItem(this.someItem);
        assertTrue(this.current.items.size() > 0);
    }

    @Test
    public void allItemTest_AuctionDateIsFuture_True() {
        this.future.addItem(this.someItem);
        assertTrue(this.future.items.size() > 0);
    }

    /* As a bidder, I want a view in brief of all items I have already bid on in an auction.
        Auction date is in the past and I have already bid on more than one item (Pass)
        Auction date is the current day and I have already bid on more than one item (Pass)
        Auction date in in the future and I have already bid on more than one item (Pass) */


    @Test
    public void allItemOnSpecifcAuction_AuctionDateIsPastAndAtLeastOneBid_True() {
        this.someItem.addBid(this.someBid);
        this.past.addItem(this.someItem);
        assertTrue(this.past.getItems().size() > 0);
    }

    @Test
    public void allItemOnSpecificAuction_AuctionDateIsCurrent_True() {
        this.someItem.addBid(this.someBid);
        this.current.addItem(this.someItem);
        assertTrue(this.current.getItems().size() > 0);
    }

    @Test
    public void allItemOnSpecificAuction_AuctionDateIsFuture_True() {
        this.someItem.addBid(this.someBid);
        this.future.addItem(this.someItem);
        assertTrue(this.future.getItems().size() > 0);
    }

    @Test
    public void allItemOnAllAuction_NoBidsPlacedOnAnyAuction_True() {
        int allItemCount = this.past.getItems().size() + this.current.getItems().size() +
                this.future.getItems().size();
        assertTrue(allItemCount == 0);
    }

    @Test
    public void allItemOnAllAuction_BidPlacedOnlyInPastOrFuture_True() {
        this.someItem.addBid(this.someBid);
        this.future.addItem(this.someItem);
        int allItemCount = this.past.getItems().size() + this.current.getItems().size() +
                this.future.getItems().size();
        assertTrue(allItemCount == 1);
    }
}