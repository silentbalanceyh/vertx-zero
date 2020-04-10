package io.vertx.up.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.util.Date;

public class PeriodTc {

    private void assertParse(final String left, final String right) {
        final Date time = Ut.parse(left);
        final Date time1 = Ut.parse(right);
        Assert.assertEquals(time, time1);
    }

    public void testParse() {
        this.assertParse("2018-07-25 16:40:56", "2018-07-25T08:40:56Z");
    }

    @Test
    public void testParse1() {
        this.assertParse("2018-07-25 16:40:56.776", "2018-07-25T16:40:56.776");
    }

    public void testParse2() {
        this.assertParse("2018-07-25 16:40:56.776", "2018-07-25T08:40:56.776Z");
    }

    @Test
    public void testParse3() {
        this.assertParse("2018-07-25", "2018-07-25");
    }

    @Test
    public void testToDate() {
        final Instant now = new Date().toInstant();
        final LocalDate date = Ut.toDate(now);
        final LocalDate date1 = LocalDate.now();
        Assert.assertEquals(date, date1);
    }

    @Test
    public void testOffset() {
        final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        final LocalDate date = Ut.toDate(Ut.parse("2018-07-25 00:00:00").toInstant());
        final LocalDateTime dateTime = Ut.toDateTime(Ut.parse(date));
        final ZonedDateTime ldtZoned = dateTime.atZone(ZoneId.systemDefault());
        System.out.println(dateTime);
        System.out.println(date);
        System.out.println(ldtZoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }
}
