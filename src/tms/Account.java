package tms;

import java.text.DecimalFormat;

/**
 * This is an abstract class that defines the common features of all account types.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public abstract class Account {
    /**
     * Account holder's (Profile) variable
     */
    private final Profile holder;

    /**
     * Account's balance (double) variable
     */
    private double balance;

    /**
     * Account's open date (Date) variable
     */
    private final Date dateOpen;

    /**
     * Parametrized constructor to initialize an Account object
     *
     * @param holder   of account
     * @param balance  to start account with
     * @param dateOpen date for the opening of account
     */
    public Account(Profile holder, double balance, Date dateOpen) {
        this.holder = holder;
        this.balance = balance;
        this.dateOpen = dateOpen;
    }

    /**
     * Updates balance when account is charged
     *
     * @param amount of money charged
     */
    public void debit(double amount) {
        this.balance -= amount;
    }

    /**
     * Updates balance when funds are added
     *
     * @param amount of money to add to account
     */
    public void credit(double amount) {
        this.balance += amount;
    }

    /**
     * Sends a string with information regarding
     * opening date, balance, and the account holder
     *
     * @return formatted string
     */
    public String toString() {
        DecimalFormat balFormat = new DecimalFormat("0.00");
        return holder.toString() + "* $" + balFormat.format(balance) + "*" + dateOpen.toString();
    }

    /**
     * Abstract method for account's monthly interest
     *
     * @return monthly interest in dollars
     */
    public abstract double monthlyInterest();

    /**
     * Abstract method for account's monthly fee
     *
     * @return monthly fee in dollars
     */
    public abstract double monthlyFee();

    /**
     * Getter method for account balance
     *
     * @return account balance as a double
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Getter method for account opening date
     *
     * @return Date object representing when account was opened
     */
    public Date getDateOpen() {
        return dateOpen;
    }

    /**
     * Getter method for account holder
     *
     * @return Profile object representing account holder
     */
    public Profile getHolder() {
        return holder;
    }
}
