package io.vertx.domain;

import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import org.junit.Test;

enum TestValue {
    A, B
}

public class MobileDerTc {

    @Test
    public void testUser() {
        final SUser user = new SUser();
        user.setMobile("15922611447");
        /* Number */
        final JsonObject object = Ux.toJson(user);
        System.err.println(object.encodePrettily());
    }

    @Test
    public void testEnum() {
        System.out.println(TestValue.A.ordinal());
        System.out.println(TestValue.B.ordinal());
    }
}
