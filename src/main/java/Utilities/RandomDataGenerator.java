package Utilities;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataGenerator {
    public static Faker faker = new Faker();

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
                return faker.name().firstName().toLowerCase() + "@gmail.com";
            case IP_ADDRESS:
                return faker.internet().ipV4Address();
            default:
                return "Data type name not available";
        }
    }

    public static String getRandomNumber(int count) {
        return faker.number().digits(count);
    }

    public static int getRandomNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static String getRandomAlphabets(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public static String getRandomWebsiteName() {
        return "https://" + RandomDataGenerator.getRandomAlphabets(10) + ".com";
    }

    public static String getRandomFutureDate() {
        return DateFormater.formatFutureDate();
    }

    public static String getRandomPastDate(){
        return DateFormater.formatPastDate();
    }

    public static String getPresentDate(){
        return DateFormater.formatPresentDate();
    }

    public static boolean getRandomBooleanValue(){
        return faker.random().nextBoolean();
    }
}
