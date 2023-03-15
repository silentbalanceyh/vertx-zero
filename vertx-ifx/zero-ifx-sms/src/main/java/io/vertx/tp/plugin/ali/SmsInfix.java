package io.vertx.tp.plugin.ali;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.ali.sms.SmsClient;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.uca.cache.Cc;

@Plugin
@SuppressWarnings("all")
public class SmsInfix implements Infix {

    private static final String NAME = "ZERO_ALI_SMS_POOL";

    private static final Cc<String, SmsClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> Infix.init("ali-sms",
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
