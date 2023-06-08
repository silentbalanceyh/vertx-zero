package io.mature.extension.uca.commerce;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.plugin.AspectSwitcher;
import io.mature.extension.uca.log.TrackIo;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.atom.element.JSix;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.mature.extension.refine.Ox.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class Arms {
    private static final Cc<String, Arms> CC_ARMS = Cc.open();
    private final transient DataAtom atom;
    private final transient HDao dao;
    private final transient TrackIo io;
    private final transient Set<ChangeFlag> types = new HashSet<ChangeFlag>() {
        {
            this.add(ChangeFlag.DELETE);
            this.add(ChangeFlag.UPDATE);
            this.add(ChangeFlag.ADD);
        }
    };
    private transient DFabric fabric;

    private Function<JsonArray, JsonArray> fnDefault;
    private Supplier<Future<JsonArray>> fnFetcher;

    public Arms(final HDao dao, final DataAtom atom) {
        this.dao = dao;
        this.atom = atom;
        this.io = TrackIo.create(atom, dao);
    }

    public static <T extends Arms> T create(final HDao dao, final DataAtom atom) {
        return (T) CC_ARMS.pick(() -> new Arms(dao, atom), atom.identifier());
    }

    protected DataAtom atom() {
        return this.atom;
    }

    protected HDao dao() {
        return this.dao;
    }

    public <T extends Arms> T bind(final DFabric fabric) {
        this.fabric = fabric;
        return (T) this;
    }

    public <T extends Arms> T bind(final ChangeFlag... types) {
        this.types.clear();
        this.types.addAll(Arrays.asList(types));
        return (T) this;
    }

    public <T extends Arms> T bindFn(final Function<JsonArray, JsonArray> fnDefault) {
        this.fnDefault = fnDefault;
        return (T) this;
    }

    public <T extends Arms> T bindFn(final Supplier<Future<JsonArray>> fnFetcher) {
        this.fnFetcher = fnFetcher;
        return (T) this;
    }

    /*
     * 两种方式读取数据
     * 1. 带条件
     * 2. 不带条件
     */
    public Future<JsonArray> saveAsync(final JsonArray recordData, final JsonObject options) {
        return this.saveAsync(null, recordData, options);
    }

    public Future<JsonArray> saveAsync(final JsonObject criteria, final JsonArray recordData, final JsonObject options) {
        LOG.Uca.info(this.getClass(), "原始输入数据：{0}，原始配置：{1}", recordData.encode(), options.encode());
        /*
         * 构造 AoHex
         * {
         *     "components": {
         *     }
         * }
         */
        final JSix hex = JSix.create(options);
        /*
         * 第一步：清洗数据
         * 1）检查数据合法性
         * 2）写日志、做过滤
         * Record[] -> JsonArray
         */
        return this.fetchAsync(criteria)

            /* 合并旧数据和新数据 */
            .compose(original -> Ux.future(Apt.create(original, recordData)))

            /* 按标识规则对比结果 */
            .compose(apt -> Ux.future(Ao.diffPure(apt, this.atom, Ox.ignorePure(this.atom)).compared()))

            /* 变更表 */
            .compose(map -> {
                /*
                 * 处理不同类型的数据
                 */
                LOG._I.report(this.getClass(), map);
                final List<Future<JsonArray>> futures = new ArrayList<>();
                /*
                 * 只执行操作类型
                 */
                if (this.types.contains(ChangeFlag.ADD)) {
                    futures.add(this.addRecord(map.get(ChangeFlag.ADD), hex.batch(ChangeFlag.ADD)));
                }
                if (this.types.contains(ChangeFlag.UPDATE)) {
                    futures.add(this.updateRecord(map.get(ChangeFlag.UPDATE), hex.batch(ChangeFlag.UPDATE)));
                }
                if (this.types.contains(ChangeFlag.DELETE)) {
                    futures.add(this.deleteRecord(map.get(ChangeFlag.DELETE), hex.batch(ChangeFlag.DELETE)));
                }
                return Fn.compressA(futures);
            });
    }

    protected Class<?> completerCls() {
        return CompleterDefault.class;
    }

    // ------------------- 下边全是私有函数 --------------------

    private Future<JsonArray> fetchAsync(final JsonObject criteria) {
        if (Objects.isNull(this.fnFetcher)) {
            if (Ut.isNil(criteria)) {
                return this.dao.fetchAllAsync().compose(Ux::futureA);
            } else {
                return this.dao.fetchAsync(Ut.valueJObject(criteria)).compose(Ux::futureA);
            }
        } else {
            return this.fnFetcher.get();
        }
    }

    private Future<JsonArray> deleteRecord(final JsonArray data, final JsonObject options) {
        return this.processAsync(data, options, (atomy, aspect) -> {
            /* Completer构造 */
            final Completer completer = Completer.create(this.completerCls(), this.dao, this.atom).bind(options);
            final JsonArray original = atomy.dataO();
            return aspect.run(original, processed -> completer.remove(processed)
                /* 变更历史 */
                .compose(nil -> this.io.procAsync(null, original, options, original.size()))
            );
        });
    }

    private Future<JsonArray> updateRecord(final JsonArray data, final JsonObject options) {
        return this.processAsync(data, options, (atomy, aspect) -> {
            /* Completer构造 */
            final Completer completer = Completer.create(this.completerCls(), this.dao, this.atom).bind(options);
            final JsonArray input = atomy.comparedU();
            /* Input / Assert */
            return aspect.run(input, processed -> completer.update(processed)
                /* 变更历史 */
                .compose(updated -> this.io.procAsync(updated, atomy.dataO(), options, updated.size()))
            );
        });
    }

    private Future<JsonArray> addRecord(final JsonArray data, final JsonObject options) {
        return this.processAsync(data, options, (atomy, aspect) -> {
            /* Completer构造 */
            final Completer completer = Completer.create(this.completerCls(), this.dao, this.atom).bind(options);
            JsonArray input = atomy.comparedA();
            /* fnDefault默认值专用函数 */
            if (Objects.nonNull(this.fnDefault)) {
                input = this.fnDefault.apply(input);
            }
            return aspect.run(input, processed -> completer.create(processed)
                /* 变更历史 */
                .compose(created -> this.io.procAsync(created, null, options, created.size()))
            );
        });
    }

    private Future<JsonArray> processAsync(final JsonArray input, final JsonObject options,
                                           final BiFunction<Apt, AspectSwitcher, Future<JsonArray>> consumer) {
        if (input.isEmpty()) {
            return Ux.future(new JsonArray());
        } else {
            final Apt apt = Ux.ruleApt(input, false);
            /* Aspect插件 */
            final AspectSwitcher aspect = new AspectSwitcher(this.atom, options, this.fabric);
            return consumer.apply(apt, aspect);
        }
    }
}
