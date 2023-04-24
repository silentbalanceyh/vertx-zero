package io.vertx.up.uca.monitor.meansure;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.web.ID;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuotaConnect {
    private static final boolean IS_MONITOR;
    private static final Node<JsonObject> node = Ut.instance(ZeroUniform.class);
    private static final Annal LOGGER = Annal.get(QuotaConnect.class);
    /*
     * Default Quota that will be mounted to health monitor
     *
     * - session: SessionMonitor
     * - instance: Verticle Monitor
     */
    static ConcurrentMap<String, Function<Vertx, Quota>> REGISTRY_CLS = new ConcurrentHashMap<>();
    private static String PATH;

    static {
        final JsonObject config = node.read();
        if (config.containsKey("monitor")) {
            IS_MONITOR = true;
            REGISTRY_CLS.put("session", SessionQuota::new);
            REGISTRY_CLS.put("instance", VerticleQuota::new);
            final String secure = Ut.visitString(config, "monitor", "secure");
            if (Ut.isNil(secure)) {
                PATH = ID.Addr.MONITOR_PATH;
            } else {
                PATH = secure + ID.Addr.MONITOR_PATH;
            }
            final JsonArray quotas = Ut.visitJArray(config, "monitor", "quota");

            /*
             * Mount to REGISTRY_CLS
             */
            if (!quotas.isEmpty()) {
                LOGGER.info("[ Hc ] Configured size: {0}, root path: {1}",
                    String.valueOf(quotas.size()), PATH);
                final StringBuilder message = new StringBuilder("[ Hc ] Initialize components: ");
                Ut.itJArray(quotas).forEach(item -> {
                    final String path = item.getString("path", null);
                    final String componentName = item.getString("component");
                    if (Ut.notNil(path) && !REGISTRY_CLS.containsKey(path)) {
                        final Class<?> componentCls = Ut.clazz(componentName, null);
                        if (Objects.nonNull(componentCls)) {
                            REGISTRY_CLS.put(path, (vertx) -> Ut.instance(componentCls, vertx));
                            message.append(MessageFormat.format("\n\t{0} = {1}", path,
                                componentCls.getName()));
                        }
                    }
                });
                message.append("\n");
                LOGGER.info(message.toString());
            }
        } else {
            IS_MONITOR = false;
        }
    }

    public static String routePath() {
        return PATH;
    }

    public static boolean monitor() {
        return IS_MONITOR;
    }
}
