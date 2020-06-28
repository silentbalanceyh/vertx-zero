package io.vertx.up.runtime.soul;

import io.vertx.up.annotations.Address;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class UriStore {
    private static final ConcurrentMap<String, UriMeta> URIS = new ConcurrentHashMap<>();
    private static UriStore INSTANCE;

    static {
        /*
         * Initialize data here.
         */
        final Set<Receipt> receipts = ZeroAnno.getReceipts();
        final ConcurrentMap<String, Receipt> receiptMap = Ut.elementMap(new ArrayList<>(receipts), Receipt::getAddress);
        final Set<Event> events = ZeroAnno.getEvents();
        /*
         * UriMeta building
         */
        for (final Event event : events) {
            /*
             * Uri Meta
             */
            final UriMeta uriMeta = new UriMeta();
            uriMeta.setMethod(event.getMethod());
            uriMeta.setUri(event.getPath());
            /*
             * Get Address
             */
            final Method method = event.getAction();
            final Annotation annotation = method.getDeclaredAnnotation(Address.class);
            final String address = Ut.invoke(annotation, "value");
            if (Objects.nonNull(address) && receiptMap.containsKey(address)) {
                /*
                 * Worker Build
                 */
                final Receipt receipt = receiptMap.get(address);
                uriMeta.setAddress(address);
                uriMeta.setWorkerMethod(receipt.getMethod());
            }
            /*
             * Cache Key calculation
             */
            final String cacheKey = uriMeta.getCacheKey();
            if (Ut.notNil(cacheKey)) {
                URIS.put(cacheKey, uriMeta);
            }
        }
    }

    private UriStore() {
    }

    /*
     * Store URIs when container boot up
     * Lazy initialized and extract from Event/Receipt
     */
    static UriStore create() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new UriStore();
        }
        return INSTANCE;
    }

    /*
     * Add UriMeta into storage
     */
    UriStore add(final UriMeta meta) {
        final String cacheKey = meta.getCacheKey();
        if (Ut.notNil(cacheKey)) {
            URIS.put(cacheKey, meta);
        }
        return this;
    }
}
