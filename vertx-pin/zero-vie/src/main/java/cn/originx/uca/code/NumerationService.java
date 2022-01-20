package cn.originx.uca.code;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Number Service 用于序号生成专用，为序号生成器
 */
public class NumerationService implements Numeration {
    static final ConcurrentMap<String, Numeration> POOL_SERVICE = new ConcurrentHashMap<>();
    /* Component Cache */
    private static final ConcurrentMap<String, Seq<String>> POOL_CODE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Seq<DataAtom>> POOL_ATOM = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Seq<Class<?>>> POOL_CLASS = new ConcurrentHashMap<>();
    private final transient String sigma;

    NumerationService(final String sigma) {
        Objects.requireNonNull(sigma);
        this.sigma = sigma;
    }

    @Override
    public Future<ConcurrentMap<ChangeFlag, Queue<String>>> clazz(
        final Class<?> clazz,
        final ConcurrentMap<ChangeFlag, JsonArray> dataMap) {
        /* dataSize */
        final int dataSize = dataMap.values().stream()
            .map(JsonArray::size)
            .reduce(0, Integer::sum);
        final ConcurrentMap<ChangeFlag, Queue<String>> mapResult = Ao.initMQueue();
        if (0 == dataSize) {
            return Ux.future(mapResult);
        } else {
            return this.clazz(clazz, dataSize).compose(serials -> {
                dataMap.forEach((flag, array) -> {
                    final int current = array.size();
                    if (0 < current) {
                        Ut.itRepeat(current, () -> {
                            final Queue<String> ref = mapResult.get(flag);
                            ref.add(serials.poll());
                        });
                    }
                });
                return Ux.future(mapResult);
            });
        }
    }

    // ---------------------- By Static Number Class -------------------

    @SuppressWarnings("all")
    public Future<Queue<String>> clazz(final Class<?> clazz, final Integer counter, final JsonObject options) {
        final Seq<Class<?>> std = Fn.pool(POOL_CLASS, clazz.getName(), () -> new SeqClass(this.sigma));
        std.bind(options);
        return std.generate(clazz, counter);
    }

    // ---------------------- By Identifier -------------------

    @Override
    public Future<Queue<String>> atom(final DataAtom atom, final Integer counter, final JsonObject options) {
        final Seq<DataAtom> dynamic = Fn.pool(POOL_ATOM, atom.identifier(), () -> new SeqAtom(this.sigma));
        dynamic.bind(options);
        return dynamic.generate(atom, counter);
    }

    // ---------------------- By Code -------------------
    /*
     * Pool Map:
     *
     * code1 = Seq ( with sigma )
     * code2 = Seq ( with sigma )
     */

    @Override
    public Future<Queue<String>> code(final String code, final Integer counter, final JsonObject options) {
        final Seq<String> fixed = Fn.pool(POOL_CODE, code, () -> new SeqCode(this.sigma));
        fixed.bind(options);
        return fixed.generate(code, counter);
    }
}
