package tms;

/**
 * This class is for maintaining the Date values and implements the Java Interface Comparable.
 *
 * @author Kaivalya Mishra, Ridwanur Sarder
 */
public class Date implements Comparable<Date> {
    private final int year;
    private final int month;
    private final int day;

    /**
     * Parametrized constructor to initialize a date
     *
     * @param year  in date
     * @param month in date
     * @param day   in date
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Compares this date object to another
     *
     * @param date to be compared to
     * @return -1 if this date is earlier than the other, 1 if it is later, and 0 if they are equal.
     */
    public int compareTo(Date date) {
        if (year > date.year) {
            return 1;
        }
        if (year < date.year) {
            return -1;
        }
        if (month > date.month) {
            return 1;
        }
        if (month < date.month) {
            return -1;
        }
        if (day > date.month) {
            return 1;
        }
        if (day < date.day) {
            return -1;
        }
        return 0;
    }

    /**
     * Returns date in string format
     *
     * @return string representation of date with month, day, and year
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Tells whether or not a date is formatted correctly
     *
     * @return true if the date is valid
     */
    public boolean isValid() {
        if(year > 9999) {
            return false;
        }
        else if (month < 1 || month > 12 || day < 1 || year < 1) {
            return false;
        } else if (month == 2) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else if ((month == 1 || month == 3 || month == 5
                || month == 7 || month == 8 || month == 10 || month == 12)) {
            return day <= 31;
        } else {
            return day <= 30;
        }
    }
}
