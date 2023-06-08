package io.vertx.mod.ke.booter;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.booting.HExtension;
import io.vertx.up.plugin.excel.ExcelInfix;
import io.vertx.up.plugin.jooq.JooqInfix;
import io.vertx.up.plugin.redis.RedisInfix;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This will
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class BtBoot {

    /*
     * Environment Init for Split Booter
     */
    static {
        /* Jooq Init */
        JooqInfix.init(Ux.nativeVertx());
        /* Excel Init */
        ExcelInfix.init(Ux.nativeVertx());
        /* Redis Infusion to disabled */
        RedisInfix.disabled();
    }

    /*
     * （New Version）Environment Init for Split Booter
     */
    public static Future<Boolean> initAsync(final String folder, final String prefix, final boolean oob) {
        // IData interface capture from the system
        final List<Future<String>> futures = new ArrayList<>();
        ioFiles(folder, prefix, oob).map(BtKit::complete).forEach(futures::add);
        return Fn.combineT(futures).compose(nil -> Ux.futureT());
    }

    private static Stream<String> ioFiles(final String folder, final String prefix, final boolean oob) {

        final List<String> files = Ut.ioFilesN(folder, null, prefix);
        final Set<HExtension> boots = HExtension.initialize();
        if (!boots.isEmpty() && oob) {
            boots.forEach(boot -> files.addAll(boot.oob(prefix)));
            // boots.forEach(boot -> files.addAll(boot.oob(prefix)));
        }
        // 并行
        return files.parallelStream().filter(BtKit::ensure);
    }
}
