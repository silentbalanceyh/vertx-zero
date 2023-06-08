package io.vertx.up.extension.router;

import io.horizon.eon.VValue;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.backbone.invoker.Invoker;
import io.vertx.up.backbone.invoker.JetSelector;
import io.vertx.up.eon.em.container.RemindType;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresGrid {

    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();
    private static final ConcurrentMap<String, RemindType> TOPIC_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> W2E = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> E2W = new ConcurrentHashMap<>();

    public synchronized static Set<Remind> wsAll() {
        return SOCKS;
    }

    public synchronized static Set<Remind> wsSecure() {
        return wsAll().stream()
            .filter(Remind::isSecure)
            .collect(Collectors.toSet());
    }

    public synchronized static Set<Remind> wsPublish() {
        // Publish
        return wsAll().stream()
            .filter(sock -> !sock.isSecure())
            .collect(Collectors.toSet());
    }

    @SuppressWarnings("all")
    public static <O> void wsInvoke(final String subscribe,
                                    final Object body,
                                    final Handler<AsyncResult<O>> handler) {
        final Remind remind = SOCKS.stream()
            .filter(item -> subscribe.equals(item.getSubscribe()))
            .findFirst().orElse(null);
        if (Objects.isNull(remind)) {
            handler.handle(Future.succeededFuture((O) body));
        } else {
            final Method method = remind.getMethod();
            final Class<?> returnType = method.getReturnType();
            final Class<?>[] params = method.getParameterTypes();
            final Class<?> param = 0 == params.length ? null : params[VValue.IDX];
            final Invoker invoker = JetSelector.invoker(returnType, param);
            invoker.handle(remind.getProxy(), method, body, handler);
        }
    }

    public synchronized static ConcurrentMap<String, RemindType> configTopic() {
        if (TOPIC_MAP.isEmpty()) {
            SOCKS.forEach(remind -> {
                final String subscribe = remind.getSubscribe();
                if (Ut.isNotNil(subscribe)) {
                    TOPIC_MAP.put(subscribe, Objects.isNull(remind.getType()) ? RemindType.TOPIC : remind.getType());
                }
            });
        }
        return TOPIC_MAP;
    }

    // Web Socket -> Event Bus
    public synchronized static String configSubscribe(final String address) {
        initializeAddress();
        return E2W.get(address);
    }

    // Event Bus -> Web Socket
    public synchronized static String configAddress(final String subscribe) {
        initializeAddress();
        return W2E.get(subscribe);
    }

    private synchronized static void initializeAddress() {
        if (W2E.isEmpty() || E2W.isEmpty() || W2E.size() != E2W.size()) {
            SOCKS.forEach(remind -> {
                final String wAddress = remind.getSubscribe();
                final String eAddress = remind.getAddress();
                if (Ut.isNotNil(wAddress) && Ut.isNotNil(eAddress)) {
                    // Web Socket = Event Bus
                    W2E.put(wAddress, eAddress);
                    // Event Bus = Web Socket
                    E2W.put(eAddress, wAddress);
                }
            });
        }
    }
}
