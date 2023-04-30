package io.horizon.uca.convert;

import io.horizon.util.HaS;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class InstantVto implements Vto<Instant> {

    @Override
    public Instant to(final Object value, final Class<?> type) {
        if (Objects.isNull(value)) {
            /*
             * Null
             */
            return null;
        } else {
            if (Instant.class == type) {
                /*
                 * Default
                 */
                return (Instant) value;
            } else if (Date.class == type) {
                /*
                 * java.util.Date -> Instant
                 */
                return ((Date) value).toInstant();
            } else if (LocalDateTime.class == type) {
                /*
                 * java.time.LocalDateTime -> Instant
                 */
                return HaS.parse((LocalDateTime) value).toInstant();
            } else if (LocalDate.class == type) {
                /*
                 * java.time.LocalDate -> Instant
                 */
                return HaS.parse((LocalDate) value).toInstant();
            } else if (LocalTime.class == type) {
                /*
                 * java.time.LocalTime -> Instant
                 */
                return HaS.parse((LocalTime) value).toInstant();
            }
            final String literal = value.toString();
            if (HaS.isDate(literal)) {
                /*
                 * String checking here for instant
                 */
                final Date parsed = HaS.parseFull(literal);
                if (Objects.isNull(parsed)) {
                    /*
                     * Could not parsing
                     */
                    return null;
                } else {
                    /*
                     * Parsing successfully
                     */
                    return parsed.toInstant();
                }
            }
        }
        return null;
    }
}
