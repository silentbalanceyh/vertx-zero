package io.vertx.up.plugin.ali;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.plugin.ali.sms.SmsClient;

@Infusion
@SuppressWarnings("all")
public class SmsInfix implements io.vertx.up.plugin.Infix {

    private static final String NAME = "ZERO_ALI_SMS_POOL";

    private static final Cc<String, SmsClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> io.vertx.up.plugin.Infix.init("ali-sms",
            (config) -> SmsClient.createShared(vertx),
            SmsInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static SmsClient getClient() {
        return CC_CLIENT.store(NAME);
    }

    @Override
    public SmsClient get() {
        return getClient();
    }
}
