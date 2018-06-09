import java.io.Serializable;

/**
 * @author Baisal
 * @version May 29, 2018
 */
public class AuctionCentralEmployee extends User implements Serializable {
    private String name;

    /**
     *
     * @param email
     * @param username
     * @param address
     * @param phoneNumber
     * @param name
     */
    public AuctionCentralEmployee(final String email, final String username,
                                  final String address, final String phoneNumber, String name) {
        super(email, username, address, phoneNumber);
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return super.username;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }
}
