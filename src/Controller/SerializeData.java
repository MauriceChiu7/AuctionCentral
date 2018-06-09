import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;

/** Responsible for serializing data and outputing bin files.
 * @author Hari Kuduva
 * @version May 29, 2018
 * */
public class SerializeData {
    /** Scanner to scan input. **/
    private static Scanner inputScanner;
    /** Formatter needed to format dates. **/
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /** Private constructor to prevent instantiation. **/
    private SerializeData() { }

    private static final String MAURICE_CONSTANT = "./JavaCode/Assets/";
    // When Maurice runs constant should be "./"
    // When Other people run constant should be "./JavaCode/Assets/"
    // ./JavaCode/Assets/

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String... args) throws IOException, ClassNotFoundException {
        /* NOTICE::: ONLY RUN THIS WHEN BIDDERS.BIN AND NPCONTACT.BIN ARE NOT IN ASSETS FOLDER
            OR IF THEIR ORIGINAL TXT FILES HAVE BEEN UPDATED (THEN DELETE THE OLD .BIN FILES AND RUN THIS) */
        serializeBidders(MAURICE_CONSTANT +"bidders.bin");
        serializeNPContact(MAURICE_CONSTANT +"npcontact.bin");
        serializeAuctions(MAURICE_CONSTANT +"auctions.bin");
        serializeAdmins(MAURICE_CONSTANT +"admins.bin");
        serializeSystemDependencies(MAURICE_CONSTANT +"system.bin");
    }

    /**
     *
     * @param output
     * @throws IOException
     */
    private static void serializeSystemDependencies(String output) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
        inputScanner = new Scanner(SerializeData.class
            .getResourceAsStream("masterSystemDetails.txt"));
        Integer maxUpcomingAuctionsAllowed = inputScanner.nextInt();
        oos.writeObject(maxUpcomingAuctionsAllowed);
    }

    /** Serializes the admins into admins.bin.
     * @param output the output destination
     * @throws IOException exception risk */
    private static void serializeAdmins(String output) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream((new FileOutputStream(output)));
        inputScanner = new Scanner(SerializeData.class
                .getResourceAsStream("masterAdminUserList.txt"));
        HashSet<AuctionCentralEmployee> toSerialize = new HashSet<>();
        while (inputScanner.hasNextLine()) {
            String parts[] = inputScanner.nextLine().split(",");
            cleanParts(parts);
            AuctionCentralEmployee test = new AuctionCentralEmployee(parts[2], parts[1], parts[3],
                    parts[4], parts[0]);
            toSerialize.add(test);
        }
        oos.writeObject(toSerialize);

    }

    /** Serializes the bidders into bidders.bin.
     * @param output the output destination
     * @throws IOException exception risk */
    private static void serializeBidders(String output) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
        inputScanner = new Scanner(SerializeData.class
                .getResourceAsStream("masterBidderList.txt"));
        HashSet<Bidder> toSerialize = new HashSet<>();
        while (inputScanner.hasNextLine()) {
            String parts[] = inputScanner.nextLine().split(",");
            cleanParts(parts);
            toSerialize.add(new Bidder(parts[0], parts[1], parts[2],
                    parts[3], parts[4], Integer.parseInt(parts[5].trim())));
        }
        oos.writeObject(toSerialize);
    }

    /** Serializes the NPContacts into npcontact.bin.
     * @param output the output destination
     * @throws IOException exception risk */
    private static void serializeNPContact(String output) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
        inputScanner = new Scanner(SerializeData.class
                .getResourceAsStream("masterNPContactList.txt"));
        HashSet<NPContact> toSerialize = new HashSet<>();
        while (inputScanner.hasNextLine()) {
            String parts[] = inputScanner.nextLine().split(",");
            cleanParts(parts);
            toSerialize.add(new NPContact(parts[0], parts[1], parts[2],
                    parts[3], parts[4]));
        }
        oos.writeObject(toSerialize);
    }

    /** Serializes the Auctions into auctions.bin.
     * @param output the output destination
     * @throws IOException exception risk */
    private static void serializeAuctions(String output) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
        inputScanner = new Scanner(SerializeData.class
                .getResourceAsStream("masterAuctionList.txt"));
        HashSet<Auction> toSerialize = new HashSet<Auction>();
        while (inputScanner.hasNextLine()) {
            String parts[] = inputScanner.nextLine().split(",");
            cleanParts(parts);
            Auction temp = new Auction(parts[0],
                    LocalDateTime.parse(parts[1], formatter),
                    LocalDateTime.parse(parts[2], formatter),
                    Integer.parseInt(parts[3]));
            addItemsForAuction(temp);
            toSerialize.add(temp);
        }
        oos.writeObject(toSerialize);
    }

    /** Adds the items for the specific auctions.
     * @param auction the specific auction */
    private static void addItemsForAuction(Auction auction) {
        Scanner scanItem = new Scanner(SerializeData.class
                .getResourceAsStream("masterItemList.txt"));
        while (scanItem.hasNextLine()) {
            String parts[] = scanItem.nextLine().split(",");
            cleanParts(parts);
            if (auction.getAuctionID() == Integer.parseInt(parts[0])) {
                Item temp = new Item(parts[1], Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]), parts[4], parts[5]);
                Scanner scanBidsOnItems = new Scanner(SerializeData.class
                .getResourceAsStream("masterItemBiddingList.txt"));
                while (scanBidsOnItems.hasNextLine()) {
                    String parts2[] = scanBidsOnItems.nextLine().split(",");
                    cleanParts(parts2);
                    if (auction.getAuctionID() == Integer.parseInt(parts2[1]) &&
                            temp.getName().equals(parts2[2])) {
                        temp.addBid(new Bid(parts2[0], temp.getName(),
                                Integer.parseInt(parts2[3]), auction.getAuctionID(),
                                auction.getStart(), auction.getEnd()));
                    }
                }
                auction.addItem(temp);
            }
        }
    }

    /** Method helps clean up scanned data.
     * @param parts the data array */
    private static void cleanParts(String parts[]) {
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
    }
}
