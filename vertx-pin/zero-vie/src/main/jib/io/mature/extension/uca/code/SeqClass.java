package io.mature.extension.uca.code;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SeqClass extends AbstractSeq<Class<?>> {

    private static final transient ConcurrentMap<Class<?>, String> POOL_CLASS = new ConcurrentHashMap<>();

    private final transient Seq<String> fixed;

    SeqClass(final String sigma) {
        super(sigma);
        this.fixed = new SeqIndent(sigma);
    }

    static void bindStatic(final Class<?> clazz, final String code) {
        if (Ut.isNotNil(code) && Objects.nonNull(clazz)) {
            POOL_CLASS.put(clazz, code);
        }
    }

    @Override
    public Seq<Class<?>> bind(final JsonObject options) {
        super.bind(options);
        /*
         * If contains `numbers` such as:
         * {
         *      "numbers": {
         *          "className": "NUM ( code )"
         *      }
         * }
         */
        final JsonObject numbers = options.getJsonObject(KName.NUMBERS, new JsonObject());
        numbers.fieldNames().forEach(className -> {
            final Class<?> clazz = Ut.clazz(className, null);
            final String code = numbers.getString(className);
            if (Objects.nonNull(clazz) && Ut.isNotNil(code)) {
                this.bind(clazz, code);
            }
        });
        return this;
    }

    @Override
    public Seq<Class<?>> bind(final Class<?> clazz, final String code) {
        bindStatic(clazz, code);
        return this;
    }

    @Override
    public Future<Queue<String>> generate(final Class<?> input, final Integer counter) {
        final String code = POOL_CLASS.getOrDefault(input, null);
        return this.fixed.generate(code, counter);
    }
}
