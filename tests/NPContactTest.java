import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class NPContactTest {

    //Fields
    private int setNumberOfDays;
    private int setNumberOfDaysTwo;
    private int setNumberOfDaysThree;
    private int setNumberOfDay;
    private int setNumberOfDayTwo;
    private int setNumberOfDayThree;

    private ArrayList<Auction> oneAuctions, twoAuctions, threeAuctions;
    private Auction auctionOne, auctionTwo, auctionThree;
    private DataControlCenter dataControlCenter;


    @Before
    public void setUp() throws Exception {

        dataControlCenter = new DataControlCenter();
        //No auction can be scheduled more than a set number of days from the current date, default of 60 days.
        //No auction can be scheduled less than a set number of days from the current date, default of 14.
        setNumberOfDays = 55; // except true
        setNumberOfDaysTwo = 60; // except true
        setNumberOfDaysThree = 61; // expect false

        setNumberOfDay = 15; // except true
        setNumberOfDayTwo = 14; // except true
        setNumberOfDayThree = 10; // except false

        oneAuctions = new ArrayList<Auction>();
        twoAuctions = new ArrayList<Auction>();
        threeAuctions = new ArrayList<Auction>();

        auctionOne = new Auction("American Cancer Society", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),  1);
        auctionTwo = new Auction("American Red Cross", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),  2);
        auctionThree = new Auction("Outlook on India", LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),  3);

        threeAuctions.add(auctionOne); threeAuctions.add(auctionTwo); threeAuctions.add(auctionThree);

        twoAuctions.add(auctionOne); twoAuctions.add(auctionTwo);

        oneAuctions.add(auctionOne);
    }

    @Test
    public void isRequestedAuctionDateValid_forMaxSetNumberOfDateIsless_true() {
        assertTrue(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDays)));
    }

    @Test
    public void isRequestedAuctionDateValid_forMaxSetNumberOfDateIsSame_true() {
        assertTrue(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDaysTwo)));
    }

    @Test
    public void isRequestedAuctionDateValid_forMaxSetNumberOfDateIsMore_false() {
        assertFalse(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDaysThree)));
    }

    @Test
    public void isRequestedAuctionDateValid_forMinSetNumberOfDateIsless_true() {
        assertTrue(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDay)));
    }

    @Test
    public void isRequestedAuctionDateValid_forMinSetNumberOfDateIsSame_true() {
        assertTrue(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDayTwo)));
    }

    @Test
    public void isRequestedAuctionDateValid_forMinSetNumberOfDateIsMore_false() {
        assertFalse(dataControlCenter.isRequestedAuctionDateValid(LocalDateTime.now().plusDays(setNumberOfDayThree)));
    }

    @Test
    public void isAuctionAvailableForSubmissionRequest_forSetOfAuctionnumbers_fail() {
        assertFalse(dataControlCenter.isAuctionAvailableForSubmissionRequest(LocalDateTime.now(), twoAuctions));
    }

    @Test
    public void isAuctionAvailableForSubmissionRequest_forSetOfAuctionnumbers_false() {
        assertFalse(dataControlCenter.isAuctionAvailableForSubmissionRequest(LocalDateTime.now(), oneAuctions));
    }





}
