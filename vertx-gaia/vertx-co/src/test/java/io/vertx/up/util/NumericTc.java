package io.vertx.up.util;

import io.horizon.util.HUt;
import org.junit.Test;

public class NumericTc {

    @Test
    public void testNumber() {
        final long start = System.currentTimeMillis();
        int counter = 0;
        for (int idx = 0; idx < 100; idx++) {
            final Integer result = HUt.randomNumber(6);
            if (6 != result.toString().length()) {
                System.out.println(result);
            }
            counter++;
        }
        final long end = System.currentTimeMillis();
        System.out.println("Successfully, spend: " + (end - start) / 1000 + "s, " + counter + " number generated.");
    }
}
