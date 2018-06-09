import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Shannon Weston
 * @version 5/7/2018
 */
public class DataControlCenter {
	/**Sets the farthest date an auction can be scheduled.*/
	private static final int MAX_SCHEDULE_OUT_DAYS = 60;
	/**Sets the soonest date an auction can be scheduled.*/
	private static final int MIN_SCHEDULE_OUT_DAYS = 14;
	/**Set the maximum number of auctions that can be scheduled in any given day.*/
	private static final int MAX_AUCTIONS_PER_DAY = 2;
	/**Time distance (minimum) between end of one auction and start of next.*/
	private static final int STOP_TO_START_HOUR_GAP = 2;
	private static final int DEFAULT_MAX_AUCTIONS = 25;
    private HashSet<Auction> addedAuctions;
    private HashSet<Auction> updatedAuctions;
    private HashSet<Auction> cancelledAuctions;
    private HashSet<Auction> biddedAuctions;
    private HashSet<Item> itemUpdates;
    private Item updateItem;
    private int nextAvailableAuctionId;
    private int maxAuctionAllowed;

    private static final String MAURICE_SPECIAL_STRING = "";

    // Maurice's special string should be "." for maurice, "" for others

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataControlCenter() throws IOException, ClassNotFoundException {
        this.addedAuctions = new HashSet<>();
        this.updatedAuctions = new HashSet<>();
        this.cancelledAuctions = new HashSet<>();
        this.maxAuctionAllowed = this.deserializeMaxUpcomingAucAllowed();
        this.nextAvailableAuctionId = findNextAvailableAuctionId();
        this.biddedAuctions = new HashSet<>();
        this.updateItem = null;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<Auction> getPastAuctions() throws IOException, ClassNotFoundException {
        HashSet<Auction> toSend = new HashSet<>();
        for (Auction auction : this.deserializeAllAuctions()) {
            if (auction.getEnd().isBefore(LocalDateTime.now())) {
                toSend.add(auction);
            }
        }
        for (Auction auction : this.cancelledAuctions) {
            if (toSend.contains(auction)) {
                toSend.remove(auction);
            }
        }
        return toSend;
    }

    /**
     *
     * @param auctionId
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Auction getAuctionById(Integer auctionId) throws IOException, ClassNotFoundException {
        for (Auction auction : this.deserializeAllAuctions()) {
            if (auction.getAuctionID() == auctionId) {
                return auction;
            }
        }
        return null;
    }

    /**
     *
     * @param auction
     * @return
     */
    public boolean cancelAuction(Auction auction) {
        int bidCount = 0;
        for (Item item : auction.getItems()) {
            bidCount += item.getBids().size();
        }
        if (bidCount == 0) {
            auction.setIsCanceled(true);
            this.cancelledAuctions.add(auction);
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private int deserializeMaxUpcomingAucAllowed() throws IOException, ClassNotFoundException {
        return (int) new ObjectInputStream(getClass().getResourceAsStream("system.bin")).readObject();
    }

    /**
     *
     * @return
     */
    public int getMaxAuctionAllowed () { return this.maxAuctionAllowed; }

    /**
     *
     * @return
     */
    public boolean isAuctionAllowed() { return (this.getMaxAuctionAllowed() > this.getAuctions().size()); }

    /**
     *
     * @param max
     * @return
     */
    public boolean setMaxAuctionAllowed (int max) {
        if (max < 0) { return false; }
        this.maxAuctionAllowed = max;
        return true;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<Auction> getCancelAbleAuctions() throws IOException, ClassNotFoundException {
        HashSet<Auction> toSend = this.getActiveAuctions();
        for (Auction auction : this.getFutureAuctions()) {
            toSend.add(auction);
        }
        for (Auction auction : this.getPastAuctions()) {
            toSend.add(auction);
        }

        HashSet<Auction> modifiedToSend = new HashSet<>();
        for (Auction auction : toSend) {
            if (!this.cancelledAuctions.contains(auction)) {
                modifiedToSend.add(auction);
            }
        }
        return modifiedToSend;
    }

    /**
     * Finds the next available auction id when creating auctions.
     * @return next available auction id
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public int findNextAvailableAuctionId() throws IOException, ClassNotFoundException {
        return this.deserializeAllAuctions().size() + this.addedAuctions.size() + 1;
    }

    /** Gets the next available auction id.
     * @return next available auction id */
    public int getNextAvailableAuctionId() {
        this.nextAvailableAuctionId++;
        return this.nextAvailableAuctionId - 1;

    }

    /** Deserializes all NPContacts from the bin file.
     * @return HashSet<NPContacts>
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk
     */
    private HashSet<NPContact> deserializeAllNPContacts() throws IOException, ClassNotFoundException {
        return (HashSet<NPContact>) new ObjectInputStream(getClass().
                getResourceAsStream("npcontact.bin")).readObject();
    }

    /** Deserializes all Bidders from the bin file.
     * @return HashSet<Bidder>
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk
     */
    private HashSet<Bidder> deserializeAllBidders() throws IOException, ClassNotFoundException {
        HashSet<Bidder> bidderSet = (HashSet<Bidder>) new ObjectInputStream(getClass().
                getResourceAsStream("bidders.bin")).readObject();
        return (HashSet<Bidder>) new ObjectInputStream(getClass().
                getResourceAsStream("bidders.bin")).readObject();
    }

    /** Deserializes all Auctions from the bin file.
     * @return HashSet<Auction>
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk
     */
    public HashSet<Auction> deserializeAllAuctions() throws IOException, ClassNotFoundException {
        HashSet<Auction> auctionSet = (HashSet<Auction>) new ObjectInputStream(getClass()
                .getResourceAsStream("auctions.bin")).readObject();
        return (HashSet<Auction>) new ObjectInputStream(getClass()
                .getResourceAsStream("auctions.bin")).readObject();
    }

    /** Returns boolean val if the bidder is valid
     * @param username bidder's username
     * @return true if bidder is valid
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk */
    public boolean isBidderValid(String username) throws IOException, ClassNotFoundException {
    	for (Bidder bidder : this.deserializeAllBidders()) {
            if (bidder.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /** Returns boolean val if the NPContact is valid
     * @param username npcontact's username
     * @return true if npcontact is valid
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk
     */
    public boolean isNonProfitValid(String username) throws IOException, ClassNotFoundException {
        for (NPContact contact : this.deserializeAllNPContacts()) {
            if (contact.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /** Gets a specific bidder by his/her username
     * @param username the username
     * @return Bidder
     * @throws IOException exception risk
     * @throws ClassNotFoundException exception risk */
    public Bidder getBidderByUsername(String username) throws IOException, ClassNotFoundException {
        for (Bidder bidder : this.deserializeAllBidders()) {
            if (bidder.getUsername().equals(username)) {
                return bidder;
            }
        }
        return null;
    }

    /** Gets a specific NPContact by his/her username
     * @param username the username
     * @return NPContact */
    public NPContact getNPContactByUsername(String username) throws IOException, ClassNotFoundException {
        for (NPContact contact : this.deserializeAllNPContacts()) {
            if (contact.getUsername().equals(username)) {
                return contact;
            }
        }
        return null;
    }

    /** Retuns a set of auctions the bidder has bids in
     * @param currBidder the current bidder
     * @return HashSet<Auction> */
    public HashSet<Auction> getAuctionsCurrBidderHasBids(Bidder currBidder) throws ClassNotFoundException, IOException {
        HashSet<Auction> toSend = new HashSet<>();
        for (Auction auction : this.deserializeAllAuctions()) {
            for (Item item : auction.getItems()) {
                for (Bid bid : item.getBids()) {
                    if (bid.getBidder().equals(currBidder.getName())) {
                            toSend.add(auction);
                    }
                }
            }
        }
        return toSend;
    }

    /** Returns a set of Items the bidder has bid on in an auction.
     * @param currBidder the bidder
     * @param specific the auction
     * @return HashSet<Item> */
    public HashSet<Item> getItemsCurrBidderHasBidsOnInAnAuction(Bidder currBidder, Auction specific) throws IOException, ClassNotFoundException {
        HashSet<Item> toSend = new HashSet<>();
        for(Auction a : this.deserializeAllAuctions()) {
	        	if(a.toString().equals(specific.toString())) {
	            	for(Item i : a.getItems()) {
	            		for(Bid b : i.getBids()) {
	            			if(b.getBidder().equals(currBidder.getName())) {
	            				toSend.add(i);
	            			}
	            		}
	            	}
	        	}
        }
        return toSend;
    }

    /** Gets a set of auctions the bidder can bid on.
     * @param currBidder the bidder
     * @return set of auctions */
    public HashSet<Auction> getAuctionsCurrBidderCanBidOn(Bidder currBidder) throws ClassNotFoundException, IOException {
        HashSet<Auction> toSend = new HashSet<>();
        //HashSet<Auction> test = this.deserializeAllAuctions();
        HashSet<Auction> allAuctions = this.getAllAuctions();
        for(Auction auc : allAuctions) {
        	if(auc.getStart().isAfter(LocalDateTime.now())) {
        		//if(a.getEnd().isAfter(LocalDateTime.now())) {
                	toSend.add(auc);
        		//}
        	}
        }

        return toSend;
    }

    /**
     * Gets all the auctions. Including past, current, and future auctions.
     * @return set of autions
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public HashSet<Auction> getAllAuctions() throws ClassNotFoundException, IOException {
        HashSet<Auction> toSend = new HashSet<>();
        for(Auction a : this.deserializeAllAuctions()) {
            toSend.add(a);
        }
        if (this.updateItem != null) {
            Auction old = this.getAuctionById(this.updateItem.getBidWithHighestBid().getAuctionID());
            Auction toReplace = new Auction(old);

            toReplace.addItem(this.updateItem);

            HashSet<Auction> temp = new HashSet<>();
            for (Item item : old.getItems()) {
                if (!item.getName().equals(updateItem.getName())){
                    toReplace.addItem(item);
                }
            }

            HashSet<Auction> newSend = new HashSet<>();
            for (Auction auction : this.deserializeAllAuctions()) {
                if (auction.getAuctionID() == toReplace.getAuctionID()) {
                    newSend.add(toReplace);
                } else {
                    newSend.add(auction);
                }
            }
            return newSend;

        }
        return toSend;
    }

    /** Gets a set of auctions that were submitted as requests by NPContact
     * @param currContact the NPContact
     * @return set of auctions */
    public HashSet<Auction> getSubmittedAuctionsByNPContact(NPContact currContact) throws ClassNotFoundException, IOException {
        HashSet<Auction> toSend = new HashSet<>();
        for(Auction a : this.deserializeAllAuctions()) {
	        	if(a.getOrganization().equals(currContact.getName())) {
	        		toSend.add(a);
	        	}
        }
        for (Auction a : this.addedAuctions) {
            toSend.add(a);
        }
        return toSend;
    }

    /**
     * Logs out the NPContact and serializes their information
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void logOutNP() throws IOException, ClassNotFoundException {
        HashSet<Auction> toSerialize = this.deserializeAllAuctions();
        for (Auction a : this.addedAuctions) {
            toSerialize.add(a);
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MAURICE_SPECIAL_STRING + "./JavaCode/Assets/auctions.bin"));
        oos.writeObject(toSerialize);
        this.addedAuctions.clear();
    }

    /** Adds an auction
     * @param auction the auction */
    public void addAuction(Auction auction) {
        this.addedAuctions.add(auction);
    }

    /** Gets the auctions.
     * @return Set of auctions */
    public HashSet<Auction> getAuctions() {
    	HashSet<Auction> allAuctions = new HashSet<Auction>();
    	for(Auction auction : this.addedAuctions) {
    		allAuctions.add(auction);
    	}

    	return allAuctions;
    }

    /** Returns true if the auction date is valid
     * @param inputDate the input date
     * @return true if date is valid */
    public boolean isRequestedAuctionDateValid(LocalDateTime inputDate) {
    	boolean available = true;
    	LocalDate earliest = LocalDate.now().plusDays(MIN_SCHEDULE_OUT_DAYS);
    	LocalDate latest = LocalDate.now().plusDays(MAX_SCHEDULE_OUT_DAYS);
    	LocalDate date = inputDate.toLocalDate();
    	
    	if(date.isBefore(earliest)) {
    		available = false;
    	}

    	if(date.isAfter(latest)) {
    		available = false;
    	}
    	return available;
    }

    /** Returns a boolean value depending if the auction date is available.
     * @param inputDate start date of auction
     * @param endDate end date of auction
     * @return true if auction date is available */
    public boolean isRequestedAuctionDateAvailable(LocalDateTime inputDate, LocalDateTime endDate) throws IOException, ClassNotFoundException {
    	boolean available = true;
    	int count = 0;
    	if(this.getAuctions().isEmpty()) {
    		return available;
    	}
        for (Auction auction : this.getAuctions()) {

        	System.out.println("existing auction: " + auction.getStart().toString());
        	System.out.println("requested: " + inputDate.toString());

            if (auction.getStart().equals(inputDate)) {
                count++;
        		if(count == MAX_AUCTIONS_PER_DAY) {
        			available = false;
        			break;
        		}
        		if(!isTimeAvailable(inputDate, endDate)) {
        			available = false;
        		}
            }
        }
    	return available;
    }

    /** Returns boolean value depending if auction time is available
     * @param inputDate start date
     * @param endDate end date
     * @return boolean */
    public boolean isTimeAvailable(LocalDateTime inputDate, LocalDateTime endDate) throws IOException, ClassNotFoundException {
    	LocalTime time = inputDate.toLocalTime();
    	LocalTime endInput = endDate.toLocalTime();
    	boolean available = true;
    	for (Auction auction : this.getAuctions()) {
            if (time.isBefore(auction.getEnd().toLocalTime().plusHours(STOP_TO_START_HOUR_GAP))) {
            	available = false;
            }
            if(endInput.isAfter(auction.getStart().toLocalTime().minusHours(STOP_TO_START_HOUR_GAP))) {
            	available = false;
            }
        }
    	return available;
    }

    /**
     *
     * @param inputDate
     * @param auctions
     * @return
     */
    public boolean isAuctionAvailableForSubmissionRequest(LocalDateTime inputDate, ArrayList<Auction> auctions) {
        int counter = 0;
        for (Auction auction : auctions) {
               if (inputDate.toLocalTime().isBefore(auction.getStart().toLocalTime()) && counter <= 2) {
                   return true;
               }
               counter++;
           }

       return false;
    }

    /**
     * Logs the bidder out and serializes the data.
     * @param currentBidder
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void logOutBidder(Bidder currentBidder) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MAURICE_SPECIAL_STRING + "./JavaCode/Assets/auctions.bin"));

        //HashSet<Auction> toSerialize = new HashSet<>();
        HashSet<Auction> toSerialize = this.getAllAuctions();

//        for (Auction replace : this.updatedAuctions) {
//            for (Auction original : this.deserializeAllAuctions()) {
//                if (replace.getAuctionID() == original.getAuctionID()) {
//                    toSerialize.add(replace);
//                }
//            }
//        }
//        for (Auction original : this.deserializeAllAuctions()) {
//            if (!toSerialize.contains(original)) {
//                toSerialize.add(original);
//            }
//        }

        HashSet<Bidder> toSerializeBidders = this.deserializeAllBidders();
        //toSerializeBidders.add(currentBidder);
//        for (Bidder bidder : this.deserializeAllBidders()) {
//            if (!bidder.getName().equals(currentBidder.getName())){
//            //if (!toSerializeBidders.contains(currentBidder)) {
//                toSerializeBidders.add(bidder);
//            }
//        }

        this.updatedAuctions.clear();
        oos.writeObject(toSerialize);

        oos = new ObjectOutputStream(new FileOutputStream(MAURICE_SPECIAL_STRING + "./JavaCode/Assets/bidders.bin"));
        oos.writeObject(toSerializeBidders);
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void logOutAdmin() throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MAURICE_SPECIAL_STRING + "./JavaCode/Assets/system.bin"));
        oos.writeObject(this.maxAuctionAllowed);
        oos = new ObjectOutputStream(new FileOutputStream(MAURICE_SPECIAL_STRING + "./JavaCode/Assets/auctions.bin"));
        HashSet<Auction> toSerialize = this.deserializeAllAuctions();
        HashSet<Auction> modifiedSerialize = new HashSet<>();
        for (Auction auction : toSerialize) {
            if (!this.cancelledAuctions.contains(auction)) {
                modifiedSerialize.add(auction);
            }
        }
        oos.writeObject(modifiedSerialize);
        this.cancelledAuctions.clear();
    }

    /**
     *
     * @param auction
     * @param item
     * @param bid
     */
    public void placeBid(Auction auction, Item item, Bid bid) {
        item.addBid(bid);
        this.updateItem = item;
    }

    /**
     *
     * @param username
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean isAdminValid(String username) throws IOException, ClassNotFoundException{
        for (AuctionCentralEmployee admin : this.deserializeAllAdmins()) {
            if (admin.getUsername().equals(username)) {
                //System.out.println(admin.getName());
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private HashSet<AuctionCentralEmployee> deserializeAllAdmins() throws IOException, ClassNotFoundException {
        return (HashSet<AuctionCentralEmployee>) new ObjectInputStream(getClass().
                getResourceAsStream("admins.bin")).readObject();
    }

    /**
     *
     * @param username
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AuctionCentralEmployee getAdminByUsername(String username) throws IOException, ClassNotFoundException {
        for (AuctionCentralEmployee admin : this.deserializeAllAdmins()) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<Auction> getActiveAuctions() throws IOException, ClassNotFoundException {
        HashSet<Auction> toSend = new HashSet<>();
        for (Auction auction : this.deserializeAllAuctions()) {
            if (auction.getEnd().isAfter(LocalDateTime.now()) &&
                    auction.getStart().isBefore(LocalDateTime.now())) {
                if (this.cancelledAuctions.contains(auction)) {
                    auction.setIsCanceled(true);
                }
                toSend.add(auction);
            }
        }
        HashSet<Auction> newSend = new HashSet<>();
        for (Auction auction : toSend) {
            if (!auction.isCanceled()) {
                newSend.add(auction);
            }
        }
        return newSend;
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<Auction> getFutureAuctions() throws IOException, ClassNotFoundException {
        HashSet<Auction> toSend = new HashSet<>();
        for (Auction auction : this.deserializeAllAuctions()) {
            if (auction.getStart().isAfter(LocalDateTime.now())) {
                if (this.cancelledAuctions.contains(auction)) {
                    auction.setIsCanceled(true);
                }
                toSend.add(auction);
            }
        }
        HashSet<Auction> newSend = new HashSet<>();
        for (Auction auction : toSend) {
            if (!auction.isCanceled()) {
                newSend.add(auction);
            }
        }
        return newSend;
    }

    /**
     *
     * @param toSort
     * @return
     */
    public ArrayList<Auction> sortAuctionSet(HashSet<Auction> toSort) {
        ArrayList<Auction> toSend = new ArrayList<>();
        for (Auction auc : toSort) { toSend.add(auc); }
        int n = toSend.size();
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (toSend.get(j).getEnd().isAfter(toSend.get(j+1).getEnd())) {
                    Auction temp = toSend.get(j);
                    toSend.set(j, toSend.get(j+1));
                    toSend.set(j+1, temp);
                }
        return toSend;
    }

    /**
     *
     * @param toSort
     * @return
     */
    public ArrayList<Auction> sortAuctionSetByStartDate (HashSet<Auction> toSort) {
        ArrayList<Auction> toSend = new ArrayList<>();
        for (Auction auc : toSort) {
            toSend.add(auc);
        }
        int n = toSend.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (toSend.get(j).getStart().isBefore(toSend.get(j + 1).getStart())) {
                    Auction temp = toSend.get(j);
                    toSend.set(j, toSend.get(j + 1));
                    toSend.set(j + 1, temp);
                }
        return toSend;
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HashSet<Auction> getAuctionsWithBounds(LocalDateTime startTime, LocalDateTime endTime)
            throws IOException, ClassNotFoundException {
        HashSet<Auction> toSend = new HashSet<>();
        if (endTime.isBefore(startTime)) {return  null;}
        for (Auction auction : this.deserializeAllAuctions()) {
            boolean isStartInclusiveOrBeyond = auction.getStart().equals(startTime)
                                                || auction.getStart().isAfter(startTime);
            boolean isEndInclusiveOrBefore = auction.getEnd().equals(endTime)
                                                || auction.getEnd().isBefore(endTime);
            if (isStartInclusiveOrBeyond && isEndInclusiveOrBefore) {
                toSend.add(auction);
            }
        }
        for (Auction auction : this.cancelledAuctions) {
            if (toSend.contains(auction)) {
                toSend.remove(auction);
            }
        }
        return toSend;
    }

    /**
     *
     * @param currBidder
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void linkBidItemsWithAuctionID(Bidder currBidder) throws IOException, ClassNotFoundException {
        for (Auction auction : this.deserializeAllAuctions()) {
            for (Item item : auction.getItems()) {
                for (Bid bid : item.getBids()) {
                    if (currBidder.getName().equals(bid.getBidder())) {
                        currBidder.addBid(bid);
                    }
                }
            }
        }

    }

    /**
     *
     * @param item
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Auction getAuctionNameByItem(String item) throws IOException, ClassNotFoundException {
        for (Auction auction : this.deserializeAllAuctions()) {
            for (Item items : auction.getItems()) {
                if (item.equals(items.getName())) {
                    return auction;
                }
            }
        }
        return null;
    }
}
