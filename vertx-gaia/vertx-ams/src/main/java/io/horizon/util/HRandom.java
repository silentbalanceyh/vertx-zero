package io.horizon.util;

import java.util.Random;
import java.util.function.Supplier;

/**
 * @author lang : 2023/4/28
 */
class HRandom {
    private static final String SEED_CHAR =
        "01234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String SEED_CAPTCHA =
        "23456789qwertyuipasdfghjkzxcvbnmQWERTYUPASDFGHJKLZXCVBNM";
    private static final String SEED_NO_DIGIT =
        "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";


    private static char randomAt(final String seed) {
        final Random random = new Random();
        return seed.charAt(random.nextInt(seed.length()));
    }

    static String randomCaptcha(final int length) {
        return randomString(length, () -> randomAt(SEED_CAPTCHA));
    }

    static String randomString(final int length) {
        return randomString(length, () -> randomAt(SEED_CHAR));
    }

    static String randomLetter(final int length) {
        return randomString(length, () -> randomAt(SEED_NO_DIGIT));
    }

    private static String randomString(int length, final Supplier<Character> supplier) {
        final StringBuilder builder = new StringBuilder();
        while (0 < length) {
            final char seed = supplier.get();
            builder.append(seed);
            length--;
        }
        return builder.toString();
    }


    static Integer randomNumber(final int length) {
        // 1. Generate seed
        final StringBuilder min = new StringBuilder();
        final StringBuilder max = new StringBuilder();
        // 2. Calculate
        min.append(1);
        for (int idx = 0; idx < length; idx++) {
            min.append(0);
            max.append(9);
        }
        // 3. min/max
        final int minValue = Integer.parseInt(min.toString()) / 10;
        final int maxValue = Integer.parseInt(max.toString());
        final Random random = new Random();
        return minValue + random.nextInt(maxValue - minValue);
    }
}
