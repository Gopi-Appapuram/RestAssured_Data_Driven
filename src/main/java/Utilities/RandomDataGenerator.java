package Utilities;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generating random data.
 */
public class RandomDataGenerator {
    public static Faker faker = new Faker();

    /**
     * Generates random data based on the provided data type.
     *
     * @param dataTypesNames The type of random data to generate.
     * @return Random data as a string.
     */
    public static String getRandomDataFor(RandomDataTypeNames dataTypesNames) {
        switch (dataTypesNames) {
            case FIRSTNAME:
                return faker.name().firstName();
            case LASTNAME:
                return faker.name().lastName();
            case FULLNAME:
                return faker.name().fullName();
            case COUNTRY:
                return faker.address().country();
            case CITYNAME:
                return faker.address().cityName();
            case EMAIL:
                return faker.name().fullName().toLowerCase() + "@" + faker.internet().domainName() + ".com";
            case IP_ADDRESS:
                return faker.internet().ipV4Address();
            default:
                return "Data type name not available";
        }
    }

    /**
     * Generates a random number with the specified number of digits.
     *
     * @param count The number of digits in the random number.
     * @return Random number as a string.
     */
    public static String getRandomNumber(int count) {
        return faker.number().digits(count);
    }

    /**
     * Generates a random number within the specified range.
     *
     * @param min The minimum value (inclusive) of the random number.
     * @param max The maximum value (exclusive) of the random number.
     * @return Random number within the specified range.
     */
    public static int getRandomNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    /**
     * Generates random alphabetic characters.
     *
     * @param count The number of alphabetic characters to generate.
     * @return Random alphabetic characters as a string.
     */
    public static String getRandomAlphabets(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    /**
     * Generates a random website name.
     *
     * @return Random website name as a string.
     */
    public static String getRandomWebsiteName() {
        return "https://" + RandomDataGenerator.getRandomAlphabets(10) + ".com";
    }

    /**
     * Generates a random future date.
     *
     * @return Random future date as a string.
     */
    public static String getRandomFutureDate() {
        return DateFormater.formatFutureDate();
    }

    /**
     * Generates a random past date.
     *
     * @return Random past date as a string.
     */
    public static String getRandomPastDate() {
        return DateFormater.formatPastDate();
    }

    /**
     * Generates the current date.
     *
     * @return Current date as a string.
     */
    public static String getPresentDate() {
        return DateFormater.formatPresentDate();
    }

    /**
     * Generates a random boolean value.
     *
     * @return Random boolean value.
     */
    public static boolean getRandomBooleanValue() {
        return faker.random().nextBoolean();
    }
}
