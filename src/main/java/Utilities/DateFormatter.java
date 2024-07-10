package Utilities;

import net.datafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for formatting dates.
 */
public class DateFormatter {

    /**
     * Generates a random future date and formats it as a string.
     *
     * @return A string representing a random future date in the format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     */
    public static String formatFutureDate() {
        Faker faker = new Faker();
        // Generate a random future date
        Date randomDate = faker.date().future(365, TimeUnit.DAYS); // Adjust the range as needed
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(randomDate);
    }

    /**
     * Generates a random past date and formats it as a string.
     *
     * @return A string representing a random past date in the format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     */
    public static String formatPastDate() {
        Faker faker = new Faker();
        // Generate a random past date
        Date randomDate = faker.date().past(365, TimeUnit.DAYS); // Adjust the range as needed
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(randomDate);
    }

    /**
     * Formats the current date as a string.
     *
     * @return A string representing the current date in the format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
     */

    public static String formatPresentDateTime() {
        // Get the current date
        Date date = new Date();
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return formatter.format(date);
    }

    public static String formatPresentDate() {
        // Get the current date
        Date date = new Date();
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }



}
