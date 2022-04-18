package io.vertx.tp.fm.refine;

import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.specification.KSpec;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public final class Fm {
    private Fm() {
    }

    public static JsonObject qrBook(final KSpec spec) {
        return FmBook.qrBook(spec);
    }

    public static FBook umBook(final KSpec spec) {
        return FmBook.umBook(spec);
    }

    public static List<FBook> umBook(final KSpec spec, final List<FBook> books) {
        return FmBook.umBook(spec, books);
    }

    public static class Log {
        public static void infoBook(final Class<?> clazz, final String pattern, final Object... args) {
            FmLog.info(clazz, "Book", pattern, args);
        }

        public static void warnBook(final Class<?> clazz, final String pattern, final Object... args) {
            FmLog.warn(clazz, "Book", pattern, args);
        }
    }
}
