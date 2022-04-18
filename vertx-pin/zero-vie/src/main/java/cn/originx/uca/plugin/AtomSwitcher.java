package cn.originx.uca.plugin;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataGroup;
import io.vertx.tp.optic.environment.Identifier;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * 动态模型筛选器，用于不同业务接口筛选统一记录专用
 * 1）只有动态 Atom 会使用
 * 2）单量 / 批量 筛选
 */
public class AtomSwitcher implements Switcher {
    /*
     * 动态 identifier
     */
    private final transient Identifier indent;
    private final transient Identity identity;
    private final transient JsonObject options = new JsonObject();

    /*
     * 不能是私有的构造函数，反射必须公有
     */
    public AtomSwitcher(final Identity identity, final JsonObject options) {
        final Class<?> instanceCls = identity.getIdentifierComponent();
        if (Objects.isNull(instanceCls)) {
            this.indent = null;
            this.identity = null;
        } else {
            if (Ut.isImplement(instanceCls, Identifier.class)) {
                this.indent = Ut.instance(instanceCls);
                this.identity = identity;
                /*
                 * 合法时才合并 options
                 * {
                 *      "header": {},
                 *      "options": {},
                 *      "data": ...
                 * }
                 * header -> sigma / appId / appKey / lang
                 * options -> serviceConfig
                 * data -> JsonObject / JsonArray
                 */
                this.options.mergeIn(Ut.valueJObject(options));
            } else {
                this.indent = null;
                this.identity = null;
            }
        }
    }

    /*
     * 根据输入数据切换
     * input
     * {
     *      "data": {}
     * }
     */
    @Override
    public Future<DataAtom> atom(final JsonObject data, final DataAtom defaultAtom) {
        /*
         * 读取默认的 defaultAtom 中的数据源
         */
        if (Objects.isNull(this.indent)) {
            /*
             * 不连接
             */
            return Ux.future(defaultAtom);
        } else {
            final JsonObject input = this.options.copy();
            input.put(KName.DATA, data);
            Ox.Log.debugUca(this.getClass(), " Identifier 选择器：{0}", this.indent.getClass());
            final JsonObject config = Ox.pluginOptions(this.indent.getClass(), input);
            return this.indent.resolve(input, config).compose(Ut.ifNil(

                /* 默认值，配置优先 */
                () -> defaultAtom,

                /* 动态驱动成功 */
                switched -> this.atom(switched, defaultAtom.rule())
            ));
        }
    }


    /*
     * 根据输入源切换
     * input
     * {
     *      "data": []
     * }
     */
    @Override
    public Future<Set<DataGroup>> atom(final JsonArray data, final DataAtom atom) {
        if (Objects.isNull(this.indent)) {
            Ox.Log.warnUca(this.getClass(), " Identifier 选择器未配置，请检查，数据：{0}", data.encode());
            return Ux.future(Ox.toGroup(atom, data));
        } else {
            final JsonObject input = this.options.copy();
            input.put(KName.DATA, data);
            Ox.Log.debugUca(this.getClass(), " Identifier 选择器（批量）：{0}", this.indent.getClass());
            final JsonObject config = Ox.pluginOptions(this.indent.getClass(), input);
            return this.indent.resolve(input, atom.identifier(), config).compose(Ut.ifNil(
                /* 默认值，配置优先 */
                HashSet::new,

                /* 动态驱动 */
                switched -> this.atom(switched, atom.rule())
            ));
        }
    }

    private DataAtom connect(final DataAtom atom, final RuleUnique unique) {
        if (Objects.isNull(unique) || Objects.isNull(atom)) {
            /*
             * RuleUnique 为空，直接返回，不连接
             */
            return atom;
        } else {
            /*
             * RuleUnique 不为空，先读取子配置
             */
            RuleUnique found = unique.child(atom.identifier());
            if (Objects.isNull(found)) {
                found = unique;
            }
            return atom.rule(found);
        }
    }

    /*
     * identifiers
     * < identifier = JsonArray > 对应数据
     *
     * 最终构造
     * Set<DataGroup> 用于后续操作，这里返回没有必要使用 Map
     */
    private Future<Set<DataGroup>> atom(final ConcurrentMap<String, JsonArray> identifiers,
                                        final RuleUnique unique) {
        final String sigma = this.identity.getSigma();
        final Set<DataGroup> resultSet = new HashSet<>();
        identifiers.forEach((identifier, dataArray) -> {
            final DataAtom atom = this.connect(Ox.toAtom(sigma, identifier), unique);
            final DataGroup group = DataGroup.create(atom);
            Ox.Log.infoUca(this.getClass(), "最终选择的（批量） identifier = {0}, sigma = {1}", identifier, sigma);
            group.add(dataArray);
            resultSet.add(group);
        });
        return Ux.future(resultSet);
    }

    private Future<DataAtom> atom(final String identifier,
                                  final RuleUnique unique) {
        final String sigma = this.identity.getSigma();
        Ox.Log.infoUca(this.getClass(), "最终选择的 identifier = {0}, sigma = {1}", identifier, sigma);
        return Ux.future(this.connect(Ox.toAtom(sigma, identifier), unique));
    }
}
