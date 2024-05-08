package Utilities;

import net.datafaker.Faker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormater {

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

    public static String formatPastDate() {
        Faker faker = new Faker();
        // Generate a random future date
        Date randomDate = faker.date().past(365, TimeUnit.DAYS); // Adjust the range as needed
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(randomDate);
    }

    public static String formatPresentDate() {
        Faker faker = new Faker();
        // Generate a random future date
        LocalDate date = LocalDate.now(); // Adjust the range as needed
        // Format the date as a string in the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(date);
    }
}
