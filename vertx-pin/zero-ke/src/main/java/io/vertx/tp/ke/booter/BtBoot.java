package io.vertx.tp.ke.booter;

import io.vertx.core.Future;
import io.vertx.tp.plugin.booting.KBoot;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.fn.Fn;
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
        /* Redis Infix to disabled */
        RedisInfix.disabled();
    }

    /*
     * （New Version）Environment Init for Split Booter
     */
    public static Future<Boolean> initAsync(final String folder, final String prefix, final boolean oob) {
        // IData interface capture from the system
        final List<Future<String>> futures = new ArrayList<>();
        ioFiles(folder, prefix, oob).map(BtKit::complete).forEach(futures::add);
        return Fn.thenCombineT(futures).compose(nil -> Ux.futureT());
    }

    private static Stream<String> ioFiles(final String folder, final String prefix, final boolean oob) {

        final List<String> files = Ut.ioFilesN(folder, null, prefix);
        final Set<KBoot> boots = KBoot.initialize();
        if (!boots.isEmpty() && oob) {
            boots.forEach(boot -> files.addAll(boot.oob(prefix)));
        }
        return files.stream().filter(BtKit::ensure);
    }
}
