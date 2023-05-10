package io.vertx.tp.ui.init;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ui.atom.UiConfig;

import static io.vertx.tp.ui.refine.Ui.LOG;

public class UiPin {

    private static final Annal LOGGER = Annal.get(UiPin.class);

    public static void init() {
        Ke.banner("「Διασύνδεση χρήστη」- ( Ui )");
        LOG.Init.info(LOGGER, "UiConfiguration...");
        UiConfiguration.init();
    }

    public static JsonArray getOp() {
        return UiConfiguration.getOp();
    }

    public static JsonArray getColumn(final String identifier) {
        return UiConfiguration.getColumn(identifier);
    }

    public static JsonArray attributes(final String identifier) {
        return UiConfiguration.attributes(identifier);
    }

    public static String keyControl() {
        final UiConfig config = UiConfiguration.getConfig();
        if (config.okCache()) {
            return config.keyControl();
        } else {
            return null;
        }
    }

    public static String keyOps() {
        final UiConfig config = UiConfiguration.getConfig();
        if (config.okCache()) {
            return config.keyOps();
        } else {
            return null;
        }
    }

    public static int keyExpired() {
        return UiConfiguration.getConfig().getCacheExpired();
    }
}
