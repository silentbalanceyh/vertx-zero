package io.macrocosm.atom.context;

import io.horizon.eon.VValue;
import io.horizon.eon.em.EmApp;
import io.horizon.exception.boot.AmbientConnectException;
import io.horizon.specification.app.HBelong;
import io.horizon.uca.cache.Cc;
import io.horizon.util.HUt;
import io.macrocosm.specification.program.HArk;
import io.macrocosm.specification.secure.HFrontier;
import io.macrocosm.specification.secure.HGalaxy;
import io.macrocosm.specification.secure.HSpace;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-06-06
 */
class KAmbientContext {
    private static final Cc<String, HArk> CC_ARK = Cc.open();

    KAmbientContext() {
    }

    HArk running() {
        final Collection<HArk> arks = CC_ARK.store().values();
        if (VValue.ONE == arks.size()) {
            return arks.iterator().next();
        }
        throw new AmbientConnectException(KAmbientContext.class);
    }

    HArk running(final String cacheKey) {
        return Optional.ofNullable(cacheKey)
            .map(CC_ARK.store()::get)
            .orElse(null);
    }

    ConcurrentMap<String, HArk> app() {
        return CC_ARK.store();
    }

    EmApp.Mode registry(final HArk ark) {
        final String cacheKey = HUt.keyApp(ark);
        CC_ARK.store().put(cacheKey, ark);
        // 注册结束后编织应用的上下文
        // 环境中应用程序超过 1 个时才执行其他判断
        final ConcurrentMap<String, HArk> store = CC_ARK.store();
        final HBelong belong = ark.owner();
        EmApp.Mode mode = EmApp.Mode.CUBE;
        if (VValue.ONE < store.size()) {
            if (belong instanceof HFrontier) {
                mode = EmApp.Mode.FRONTIER;        // Frontier
            } else if (belong instanceof HGalaxy) {
                mode = EmApp.Mode.GALAXY;          // Galaxy
            } else if (belong instanceof HSpace) {
                mode = EmApp.Mode.SPACE;           // Space
            }
        }
        return mode;
    }
}
