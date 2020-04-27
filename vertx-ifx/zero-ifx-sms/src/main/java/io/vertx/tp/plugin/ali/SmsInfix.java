package io.vertx.tp.plugin.ali;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.ali.sms.SmsClient;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("all")
public class SmsInfix implements Infix {

    private static final String NAME = "ZERO_ALI_SMS_POOL";

    private static final ConcurrentMap<String, SmsClient> CLIENTS
            = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name, () -> Infix.init("ali-sms",
                (config) -> SmsClient.createShared(vertx),
                SmsInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static SmsClient getClient() {
        return CLIENTS.get(NAME);
    }

    @Override
    public SmsClient get() {
        return getClient();
    }
}
