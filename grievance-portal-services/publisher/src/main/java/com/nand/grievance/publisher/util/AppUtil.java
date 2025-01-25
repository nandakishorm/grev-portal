package com.nand.grievance.publisher.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AppUtil {

    public static Date convertStringToDate(String dateString, String pattern) throws ParseException {
        // Set the desired date pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        // Strict parsing with lenient set to false for strict date validation
        dateFormat.setLenient(false);

        // Parse the string to a Date object
        return dateFormat.parse(dateString);
    }

    public static String generateRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static Long generate8DigitRandomNumber() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Get the least significant bits (64 bits) of the UUID
        long leastSignificantBits = uuid.getLeastSignificantBits();

        // Ensure the generated number is positive
        long positiveNumber = Math.abs(leastSignificantBits);

        // Extract the last 8 digits
        long eightDigitNumber = positiveNumber % 100000000;

        // Display the generated unique random number
        System.out.println("Unique Random Number: " + String.format("%08d", eightDigitNumber));

        return eightDigitNumber;
    }

}
