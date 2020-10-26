package tms;

/**
 * Checking account class.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class Checking extends Account {
    /**
     * Indicates whether or not Checking account uses Direct Deposit
     */
    private final boolean directDeposit;

    /**
     * Parametrized constructor to initialize a checking account
     *
     * @param holder        of account
     * @param balance       to start account with
     * @param dateOpen      date the account was opened
     * @param directDeposit whether or not account has direct deposit
     */
    public Checking(Profile holder, double balance, Date dateOpen, boolean directDeposit) {
        super(holder, balance, dateOpen);
        this.directDeposit = directDeposit;
    }


    /**
     * Returns a string representation of the information held in the checking account
     */
    @Override
    public String toString() {
        return directDeposit ?
                "*Checking*" + super.toString() +
                        "*direct deposit account*" : "*Checking*" + super.toString();
    }


    /**
     * Returns the monthly interest for a checking account
     */
    @Override
    public double monthlyInterest() {
        return this.getBalance() * 0.0005 / 12;
    }


    /**
     * Returns the monthly fee for a checking account
     */
    @Override
    public double monthlyFee() {
        return this.getBalance() >= 1500 || this.directDeposit ? 0 : 25;
    }
}
