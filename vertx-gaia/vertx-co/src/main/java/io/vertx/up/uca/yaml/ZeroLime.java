package io.vertx.up.uca.yaml;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.cache.Cd;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang
 */
public class ZeroLime implements Node<ConcurrentMap<String, String>> {

    private static final Annal LOGGER = Annal.get(ZeroLime.class);

    private static final Cc<String, String> CC_INTERNAL = Cc.open();

    static {
        final Cd<String, String> dataRef = CC_INTERNAL.store();
        dataRef.data(KName.Internal.ERROR, ZeroTool.nameZero(KName.Internal.ERROR));
        dataRef.data(KName.Internal.INJECT, ZeroTool.nameZero(KName.Internal.INJECT));
        dataRef.data(KName.Internal.SERVER, ZeroTool.nameZero(KName.Internal.SERVER));
        dataRef.data(KName.Internal.RESOLVER, ZeroTool.nameZero(KName.Internal.RESOLVER));
    }

    private transient final Node<JsonObject> node
        = Ut.singleton(ZeroVertx.class);

    @Override
    public ConcurrentMap<String, String> read() {
        // 1. Read all zero configuration: zero
        final JsonObject data = this.node.read();
        // 2. Read the string node "lime" for extensions
        return this.build(data.getString(KName.Internal.LIME));
    }

    private ConcurrentMap<String, String> build(final String literal) {
        final Set<String> sets = Ut.toSet(literal, Strings.COMMA);
        LOGGER.debug("Lime node parsing \"{0}\" and size is = {1}", literal, sets.size());
        Fn.safeNull(() -> Observable.fromIterable(sets)
            .subscribe(item -> CC_INTERNAL.pick(() -> ZeroTool.nameZero(item), item)
                // Fn.po?l(INTERNALS, item, () -> ZeroTool.produce(item))\
            ).dispose(), literal);
        return CC_INTERNAL.store().data();
    }
}
