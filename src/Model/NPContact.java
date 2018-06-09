import java.io.Serializable;

/** Represents a Non-profit organization.
 * @author Hari G Kuduva
 * @version May 29, 2018
 */
public class NPContact extends User implements Serializable {

    /** Organization name of non profit. **/
    private String orgName;

    /** Creates a NPContact with 5 parameters
     * @param orgName organization name
     * @param email organization email
     * @param username organization username
     * @param address organization address
     * @param phoneNumber organization phone number */
    public NPContact(String orgName, String email,
                     String username, String address, String phoneNumber) {

    	super(email, username, address, phoneNumber);
        this.orgName = orgName;
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
        return this.orgName;
    }
}
