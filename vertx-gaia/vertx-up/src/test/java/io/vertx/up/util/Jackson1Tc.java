package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.unity.Ux;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

public class Jackson1Tc extends ZeroBase {

    @Test
    public void testLocalTime(final TestContext context) {
        final JsonObject data = Ut.ioJObject(this.ioString("pojo.json"));
        final PojoEntity entity = Ux.fromJson(data, PojoEntity.class);
        final Date endDate = Ut.parse(data.getString("end"));
        final Date startDate = Ut.parse(data.getString("start"));
        System.out.println(TimeZone.getDefault());
        System.out.println("---- Actual ------------");
        System.out.println(data.getString("end"));
        System.out.println(entity.getEnd());
        System.out.println("---- Expected ----------");
        System.out.println(endDate);
        System.out.println(Ut.toDateTime(endDate));
        System.out.println("---- Actual ------------");
        System.out.println(data.getString("start"));
        System.out.println(entity.getStart());
        System.out.println("---- Expected ----------");
        System.out.println(startDate);
        System.out.println(Ut.toDateTime(startDate));
    }
}
