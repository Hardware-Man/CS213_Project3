package tms;

/**
 * This is an abstract class that defines the common features of all account types.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class MoneyMarket extends Account {
    /**
     * Number of withdrawals from Money Market account
     */
    private int withdrawals;

    /**
     * Parametrized constructor to initialize a Money Market account
     *
     * @param holder      of account
     * @param balance     to start account with
     * @param dateOpen    date account was opened
     * @param withdrawals number of times this account has been withdrawn from
     */
    public MoneyMarket(Profile holder, double balance, Date dateOpen, int withdrawals) {
        super(holder, balance, dateOpen);
        this.withdrawals = withdrawals;
    }

    /**
     * Override of Account debit method, will perform like in superclass
     * but also increment the number of withdrawals for this account
     */
    @Override
    public void debit(double amount) {
        this.withdrawals++;
        super.debit(amount);
    }


    /**
     * Returns a string representation of the information
     * held in Money Market account
     */
    @Override
    public String toString() {
        return "*Money Market*"
                + super.toString() + "*" + withdrawals + " withdrawals*";
    }


    /**
     * Returns monthly interest of Money Market account
     */
    @Override
    public double monthlyInterest() {
        return this.getBalance() * 0.0065 / 12;
    }


    /**
     * Returns monthly fee of Money Market account
     */
    @Override
    public double monthlyFee() {
        return this.getBalance() >= 2500 && this.withdrawals <= 6 ? 0 : 12;
    }
}
