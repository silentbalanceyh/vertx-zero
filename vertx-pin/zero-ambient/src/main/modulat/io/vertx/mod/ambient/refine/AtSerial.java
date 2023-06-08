package io.vertx.mod.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

class AtSerial {
    /**
     * The basic rule for number generation
     * 1) format: the expression such as : `${var}`, the system
     * will use common-expression parser to parse and generate.
     * ${prefix}: Prefix stored in PREFIX
     * ${suffix}: Suffix stored in SUFFIX
     * ${time}:   The timestamp that will apply based on TIME format pattern
     * ${seed}:   The seed calculated by I_LENGTH, I_STEP, IS_DECREMENT
     *
     * @param number the system serial definition
     * @param count  the number size that will be generated.
     */
    static List<String> generate(final XNumber number,
                                 final Integer count) {
        final List<String> numbers = new ArrayList<>();
        final JsonObject params = new JsonObject();
        /* prefix & suffix */
        if (null != number.getPrefix()) {
            params.put("prefix", number.getPrefix());
        }
        if (null != number.getSuffix()) {
            params.put("suffix", number.getSuffix());
        }
        Long seed = number.getCurrent();
        for (int idx = 0; idx < count; idx++) {
            final JsonObject paramMap = params.copy();
            /* time */
            final String time = getTime(number);
            if (Ut.isNotNil(time)) {
                paramMap.put("time", time);
            }
            /* seed */
            final String seedStr = getSeed(number, seed);
            paramMap.put("seed", seedStr);
            /* Expression filling based on seed */
            seed = getNext(number, seed);
            /*
             * The final calculated result.
             */
            final String result = Ut.fromExpression(number.getFormat(), paramMap);
            numbers.add(result);
        }
        return numbers;
    }

    static XNumber adjust(final XNumber number, final Integer count) {
        final boolean renewal = !Objects.isNull(number.getRenewal()) && number.getRenewal();
        final int step = Objects.isNull(number.getStep()) ? 1 : number.getStep();
        final long adjust = (long) count * step;
        final boolean decrement = Objects.isNull(number.getDecrement()) || number.getDecrement();
        // No renewal setting here
        long result;
        if (decrement) {
            result = number.getCurrent() - adjust;
        } else {
            result = number.getCurrent() + adjust;
        }
        if (renewal) {
            Objects.requireNonNull(number.getTime());
            // Enable Renewal
            final int length = Objects.requireNonNull(number.getLength());
            if (0 < length) {
                final StringBuilder digest = new StringBuilder();
                Ut.itRepeat(length, () -> digest.append("9"));
                final long max = Long.parseLong(digest.toString());
                // Refresh to 1 to loop it.
                if (result > max) {
                    result = 1;
                }
            }
        }
        number.setCurrent(result);
        return number;
    }


    private static Long getNext(final XNumber number, final Long current) {
        /* Read step */
        final Integer step = number.getStep();
        final Boolean decrement = number.getDecrement();
        final long seed;
        if (decrement) {
            /* decrement */
            seed = current - step;
        } else {
            /* increment */
            seed = current + step;
        }
        return seed;
    }

    private static String getSeed(final XNumber number, final Long current) {
        /*
         * Read next step value based on current number definition
         */
        final Long seed = getNext(number, current);
        // TODO: The range that may be done in future
        return Ut.fromAdjust(Integer.parseInt(seed.toString()), number.getLength());
    }

    private static String getTime(final XNumber number) {
        String literal = "";
        if (!Objects.isNull(number.getTime())) {
            final DateFormat format = new SimpleDateFormat(number.getTime());
            literal = format.format(new Date());
        }
        return literal;
    }
}
