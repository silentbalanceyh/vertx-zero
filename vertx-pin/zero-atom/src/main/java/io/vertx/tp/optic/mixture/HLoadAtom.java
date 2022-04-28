package io.vertx.tp.optic.mixture;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.cv.AoMsg;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.phantom.AoPerformer;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HLoad;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadAtom implements HLoad {
    @Override
    public HAtom atom(final String appName, final String identifier) {
        /*
         * 每次创建一个新的 DataAtom
         * - DataAtom 属于 Model 的聚合体，它的核心两个数据结构主要是 Performer 和 Model，这两个结构在底层做了
         * 池化处理，也就是说：访问器 和 模型（底层）都不会出现多次创建的情况，那么为什么 DataAtom 要创建多个呢？
         * 主要原因是 DataAtom 开始出现了分离，DataAtom 中的 RuleUnique 包含了两部分内容
         *
         * 1. RuleUnique 来自 Model （模型定义），Master 的 RuleUnique
         * 2. RuleUnique 来自 connect 编程模式，Slave 的 RuleUnique
         * 3. 每个 RuleUnique 的结构如
         *
         * {
         *      "...":"内容（根）",
         *      "children": {
         *          "identifier1": {},
         *          "identifier2": {}
         *      }
         * }
         *
         * 通道如果是静态绑定，则直接使用 children 就好
         * 而通道如果出现了动态绑定，IdentityComponent 实现，则要根据切换过后的 DataAtom 对应的 identifier 去读取相对应的
         * 标识规则。
         *
         * 所以，每次get的时候会读取一个新的 DataAtom 而共享其他数据结构。
         */
        try {
            /*
             * Performer processing to expose exception
             */
            final String unique = Ao.toNS(appName, identifier); // Model.namespace(appName) + "-" + identifier;
            final AoPerformer performer = AoPerformer.getInstance(appName);
            final Model model = AoCache.CC_MODEL.pick(() -> performer.fetch(identifier), unique);
            // Fn.po?l(AoCache.POOL_MODELS, unique, () -> performer.fetchModel(identifier));
            /*
             * Log for data atom and return to the reference.
             */
            final DataAtom atom = new DataAtom(model, appName);
            Ao.infoAtom(DataAtom.class, AoMsg.DATA_ATOM, unique, model.toJson().encode());
            return atom;
        } catch (final _404ModelNotFoundException ignored) {
            /*
             * 这里的改动主要基于动静态模型同时操作导致，如果可以找到Model则证明模型存在于系统中，这种
             * 情况下可直接初始化DataAtom走标准流程，否则直接返回null引用，使得系统无法返回正常模型，
             * 但不影响模型本身的执行。
             */
            return null;
        }
    }
}
