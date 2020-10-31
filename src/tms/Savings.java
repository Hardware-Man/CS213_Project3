package tms;

/**
 * Savings account class.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class Savings extends Account {
    /**
     * Indicates whether or not Savings account user is loyal or not
     */
    private final boolean isLoyal;

    /**
     * Parametrized constructor to initialize a savings account
     *
     * @param holder   of savings account
     * @param balance  account starts with
     * @param dateOpen date of opening of account
     * @param isLoyal  if the account holder is loyal
     */
    public Savings(Profile holder, double balance, Date dateOpen, boolean isLoyal) {
        super(holder, balance, dateOpen);
        this.isLoyal = isLoyal;
    }


    /**
     * Returns a string with the information regarding the savings account
     */
    @Override
    public String toString() {
        return isLoyal ?
                "*Savings*" + super.toString()
                        + "*special Savings account*" : "*Savings*" + super.toString();
    }


    /**
     * Returns the monthly interest of a savings account
     */
    @Override
    public double monthlyInterest() {
        return this.isLoyal ? this.getBalance() * 0.0035 / 12 : this.getBalance() * 0.0025 / 12;
    }


    /**
     * Returns the monthly fee of a savings account
     */
    @Override
    public double monthlyFee() {
        return this.getBalance() >= 300 ? 0 : 5;
    }

    public boolean getLoyal() {
        return isLoyal;
    }
}
