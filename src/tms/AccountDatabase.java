package tms;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

/**
 * This is an array-based container class with an initial capacity of 5. It will automatically grow the capacity by 5
 * if the database is full. The array shall hold different account instances in Checking, Savings or MoneyMarket.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class AccountDatabase {
    /**
     * Array storing the accounts in AccountDatabase
     */
    private Account[] accounts;

    /**
     * Variable indicating current number of accounts stored
     */
    private int size;

    /**
     * Constructor to initialize an AccountDatabase
     */
    public AccountDatabase() {
        accounts = new Account[5];
        size = 0;
    }

    /**
     * Finds an account in the database and returns its index.
     *
     * @param account to search for
     * @return index of account if found, -1 otherwise
     */
    private int find(Account account) {
        StringTokenizer targetTokens = new StringTokenizer(account.toString(), "*");
        String targetAccType = targetTokens.nextToken();
        String targetProfileHolder = targetTokens.nextToken();
        for (int i = 0; i < size; i++) {
            StringTokenizer tokens = new StringTokenizer(accounts[i].toString(), "*");
            if (targetAccType.equals(tokens.nextToken())) {
                if (targetProfileHolder.equals(tokens.nextToken())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Increases capacity of database when database exceeds current capacity.
     */
    private void grow() {
        Account[] replaceAccounts = new Account[this.accounts.length + 5];
        System.arraycopy(this.accounts, 0, replaceAccounts, 0, this.accounts.length);
        this.accounts = replaceAccounts;
    }

    /**
     * Adds an account to the database if it doesn't already exist in it.
     *
     * @param account to be added
     * @return false if account already exists in the database, true if add is successful.
     */
    public boolean add(Account account) {
        if (find(account) != -1) {
            return false;
        }
        if (size == accounts.length) {
            grow();
        }
        accounts[size] = account;
        size++;
        return true;
    }

    /**
     * Removes an account from the database if it's in it
     *
     * @param account to search for and remove
     * @return true if account is found and removed,
     * false if account doesn't exist in database
     */
    public boolean remove(Account account) {
        int accountPosition = find(account);
        if (accountPosition == -1) {
            return false;
        }
        accounts[accountPosition] = accounts[size - 1];
        accounts[size - 1] = null;
        size--;
        return true;
    }

    /**
     * Deposits money into account if it exists in database
     *
     * @param account to receive money
     * @param amount  of money to deposit
     * @return true if account exists, false otherwise
     */
    public boolean deposit(Account account, double amount) {
        int accountPosition = find(account);
        if (accountPosition == -1) {
            return false;
        }
        accounts[accountPosition].credit(amount);
        return true;
    }

    /**
     * Withdraws money from an account in database if it exists
     *
     * @param account to withdraw from
     * @param amount  to take from account
     * @return -1 if account doesn't exist,
     * 1 if insufficient funds, and 0 if successful withdrawal
     */
    public int withdrawal(Account account, double amount) {
        int accountPosition = find(account);
        if (accountPosition == -1) {
            return -1;
        }
        double currBal = accounts[accountPosition].getBalance();
        if (currBal < amount) {
            return 1;
        }
        accounts[accountPosition].debit(amount);
        return 0;
    }

    /**
     * Sorts the database in ascending order by date accounts were opened.
     */
    private void sortByDateOpen() {
        //selection sort
        for (int i = 0; i < size - 1; i++) {
            int earliestDateIndex = i;
            for (int j = i + 1; j < size; j++)
                if (accounts[j].getDateOpen().compareTo(accounts[earliestDateIndex].getDateOpen()) < 0) {
                    earliestDateIndex = j;
                }
            Account acc = accounts[earliestDateIndex];
            accounts[earliestDateIndex] = accounts[i];
            accounts[i] = acc;
        }
    }

    /**
     * Sort database in ascending order based on last name of account holder
     */
    private void sortByLastName() {
        //selection sort
        for (int i = 0; i < size - 1; i++) {
            int alphaSmallerIndex = i;
            for (int j = i + 1; j < size; j++)
                if (accounts[j].getHolder().getLastNameFirstName().compareTo(accounts[alphaSmallerIndex].getHolder().getLastNameFirstName()) < 0) {
                    alphaSmallerIndex = j;
                }
            Account acc = accounts[alphaSmallerIndex];
            accounts[alphaSmallerIndex] = accounts[i];
            accounts[i] = acc;
        }
    }

    /**
     * Prints the database sorted by date accounts were opened in addition
     * to information regarding interest, fee, and balance
     */
    public String printByDateOpen() {
        StringBuilder result = new StringBuilder("--Printing statements by date opened--\n\n");
        sortByDateOpen();
        result.append(orderedPrint());
        return result.toString();
    }

    /**
     * Prints the database sorted by last name and information regarding
     * interest, fee, and balance
     */
    public String printByLastName() {
        StringBuilder result = new StringBuilder("--Printing statements by last name--\n\n");
        sortByLastName();
        result.append(orderedPrint());
        return result.toString();
    }

    private String orderedPrint() {
        StringBuilder result = new StringBuilder();
        DecimalFormat moneyFormat = new DecimalFormat("0.00");
        for (int i = 0; i < size; i++) {
            double accountInterest = accounts[i].monthlyInterest();
            double accountFee = accounts[i].monthlyFee();
            double newAccountBalance = accounts[i].getBalance() - accountFee + accountInterest;
            result.append(accounts[i].toString()).append("\n-interest: $ ")
                    .append(moneyFormat.format(accountInterest)).append("\n-fee: $ ")
                    .append(moneyFormat.format(accountFee)).append("\n-new balance: $ ")
                    .append(moneyFormat.format(newAccountBalance)).append("\n\n");
        }
        result.append("--end of printing--");
        return result.toString();
    }

    /**
     * Prints accounts in the database
     */
    public String printAccounts() {
        StringBuilder result;
        result = new StringBuilder(("--Listing accounts in the database--\n"));
        for (int i = 0; i < size; i++) {
            result.append(accounts[i].toString()).append("\n");
        }
        result.append("--end of listing--");
        return result.toString();
    }

    public Account[] getAccounts() {
        return accounts;
    }
}
