package cn.originx.uca.code;

import cn.vertxup.ambient.domain.tables.daos.XNumberDao;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import cn.vertxup.ambient.service.DatumService;
import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class NumerationService implements Numeration {
    private final transient DataAtom atom;
    private final transient ConcurrentMap<Class<?>, String> definition = new ConcurrentHashMap<>();
    private final transient JsonObject options = new JsonObject();
    private final transient DatumStub stub = Ut.singleton(DatumService.class);

    public NumerationService(final DataAtom atom) {
        this(atom, new JsonObject());
    }

    public NumerationService(final DataAtom atom, final JsonObject options) {
        this.atom = atom;
        this.options.mergeIn(options);
    }

    protected DataAtom atom() {
        return this.atom;
    }

    public void define(final Class<?> clazz, final String defaultValue) {
        if (Ut.isNil(this.options)) {
            /*
             * 没有 options，直接设置 clazz = defaultValue
             */
            this.definition.put(clazz, defaultValue);
        } else {
            /*
             * 定义了结构
             * {
             *      "numbers": {
             *          "clazzName": "NUM"
             *      }
             * }
             */
            final JsonObject numbers = this.options.getJsonObject(KName.NUMBERS);
            if (Ut.isNil(numbers)) {
                /*
                 * options -> numbers
                 * 未定义
                 */
                this.definition.put(clazz, defaultValue);
            } else {
                final String configValue = numbers.getString(clazz.getName());
                if (Ut.isNil(configValue)) {
                    /*
                     * options -> numbers 中
                     * 未包含 `clazz` 的 key 信息
                     */
                    this.definition.put(clazz, defaultValue);
                } else {
                    /*
                     * options -> numbers 中定义了另外的
                     * `clazz` = NUM 的配置
                     */
                    this.definition.put(clazz, configValue);
                }
            }
        }
    }

    @Override
    public Future<ConcurrentMap<ChangeFlag, Queue<String>>> numberAsync(
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
            return this.numberAsync(clazz, dataSize).compose(serials -> {
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

    @Override
    public Future<String> numberAsync(final Class<?> clazz) {
        return this.numberAsync(clazz, 1)
            .compose(queue -> Ux.future(queue.poll()));
    }

    @SuppressWarnings("all")
    public Future<Queue<String>> numberAsync(final Class<?> clazz, final Integer counter) {
        final String sigma = this.atom.sigma();
        String code = this.definition.get(clazz);
        if (Ut.isNil(code)) {
            code = NumerationMap.NUM_MAP.get(clazz);
        }
        return this.stub.numbersBySigma(sigma, code, counter)
            /*
             * 处理最终的序号相关信息
             */
            .compose(numberArray -> Ux.future(new ConcurrentLinkedQueue<>(numberArray.getList())));
    }

    @Override
    public Future<String> numberByIdAsync(final String identifier) {
        return this.fetchNumber(identifier)
            .compose(Ut.ifNil(() -> null, (number) -> this.numberAsync(number.getCode())));
    }

    @Override
    @SuppressWarnings("all")
    public Future<Queue<String>> numberByIdAsync(final String identifier, final Integer counter) {
        return fetchNumber(identifier)
            .compose(Ut.ifNil(() -> null, (number) -> this.stub.numbersBySigma(this.atom.sigma(), number.getCode(), counter)))
            .compose(numberArray -> Objects.isNull(numberArray) ?
                Ux.future(new ConcurrentLinkedQueue<>()) :
                Ux.future(new ConcurrentLinkedQueue<>(numberArray.getList())));
    }

    private Future<XNumber> fetchNumber(final String identifier) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.SIGMA, this.atom.sigma());
        filters.put(KName.IDENTIFIER, identifier);
        filters.put(Strings.EMPTY, Boolean.TRUE);
        return Ux.Jooq.on(XNumberDao.class)
            .fetchOneAsync(filters);
    }

    @Override
    public Future<String> numberAsync(final String code) {
        return this.stub.numbersBySigma(this.atom.sigma(), code, 1)
            /*
             * 处理最终序号相关信息
             */
            .compose(numberArray -> Ux.future(numberArray.getList().get(Values.IDX)))
            .compose(number -> Ux.future(number.toString()));
    }
}
