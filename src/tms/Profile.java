package tms;

/**
 * This class defines the profile of an account holder.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class Profile {
    /**
     * Profile's first name
     */
    private final String fName;

    /**
     * Profile's last name
     */
    private final String lName;

    /**
     * Initializes a constructor for a profile of
     * an account holder given the first and last name
     *
     * @param fName First name of account holder
     * @param lName Last name of account holder
     */
    public Profile(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }

    /**
     * Returns string for this Profile object
     *
     * @return string representing the profile of the account holder
     */
    public String toString() {
        return fName + " " + lName;
    }

    /**
     * Return whether or not object is equal to this profile
     *
     * @param obj to be compared to
     * @return Whether or not object equals this profile
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Profile) {
            Profile aProfile = (Profile) obj;
            return fName.equals(aProfile.fName) && lName.equals(aProfile.lName);
        }
        return false;
    }

    /**
     * Returns string containing profile owner's last and first name
     *
     * @return Formatted string with profile owner's last name then first name
     */
    public String getLastNameFirstName() {
        return lName + fName;
    }
}
