package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.commune.rule.RuleUnique;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AoCompare {
    private AoCompare() {
    }

    /*
     * Database Change
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPure(
            final JsonArray queueOld, final JsonArray queueNew,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        final RuleUnique unique = atom.ruleSmart();
        Ao.infoUca(AoCompare.class, "（Pure）对比用标识规则：\n{0}", unique.toString());
        return null;
    }

    /*
     * Database -> Integration
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPush(
            final JsonArray queueOld, final JsonArray queueNew,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        final RuleUnique unique = atom.ruleSmart();
        Ao.infoUca(AoCompare.class, "（Push）对比用标识规则：\n{0}", unique.toString());
        return null;
    }

    /*
     * Integration -> Database
     *
     * 「TP」= UCMDB
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPull(
            final JsonArray queueOld, final JsonArray queueNew,
            final DataAtom atom, final Set<String> ignoreSet
    ) {
        /*
         * 1. 任务接入
         * -- UCMDB执行 DataAtom 的主标识规则（Rule来自模型）
         * -- 第三方集成执行通道内的标识规则（Rule来自通道）
         * 2. 只有集成拉取数据会遇到 Strong / Weak 的强弱连接
         */
        final RuleUnique rule = atom.ruleSmart();
        Ao.infoDiff(AoCompare.class, "（Pull）对比用标识规则：\n{0}", rule.toString());
        final JsonArray queueA = new JsonArray();
        final JsonArray queueUTemp = new JsonArray();
        final JsonArray queueD = new JsonArray();
        /*
         * 遍历 queueOld，生成：DELETE / UPDATE
         * DELETE：旧的有值 = value，新的无值 = null
         * UPDATE：旧的有值 = value，新的有值 = value
         *
         * RULE-1：CMDB平台规则              -- code         - record
         * RULE-2：「TP」规则（集成端规则）    -- globalId     - integration
         * RULE-3：业务规则                                  - priority
         *         -- serial：服务器
         *         -- name：DDI ip address
         */
        Ut.itJArray(queueOld).forEach(recordO -> {
            /*
             * 旧记录 recordO, 新队列 queueNew
             * 拉取新记录「必须包含规则2」
             * x 2 3 - 无code，从「TP」导入后直接拉取
             * 1 2 3 - 有code，已经和「TP」集成过一次
             *
             * 1）先查询旧记录状态：
             * 草稿 / 非草稿
             * record 检查：可推送规则，可推送 - 非草稿 / 不可推送 - 草稿
             * 草稿数据不参与拉取集成
             *
             * 2）草稿数据特征
             * 草稿数据不包含 globalId（UCMDB ID）
             *
             * 旧数据，同时满足 RULE-1 AND RULE-3
             */
            final JsonObject twinO = Ux.ruleAll(rule.rulePush(), recordO);
            if (Objects.isNull(twinO)) {
                /*
                 * 单独的草稿
                 *
                 * 1. 旧数据
                 *
                 * 旧数据：
                 * 1 x 3 - 无globalId，「TP」业务标识规则已满足，但推送失败（异常数据）
                 * 1 2 3 - 有globalId，「TP」数据已经对齐了
                 * 1 x x - 无globalId，「TP」纯草稿数据，业务标识规则未满足
                 * 新记录：
                 * x 2 3 - 无code，从「TP」导入后直接拉取
                 * 1 2 3 - 有code，已经和「TP」集成过一次
                 *
                 * 旧数据不满足的情况
                 *
                 * - RULE-1 = null,   RULE-3 = value （不可能出现，旧数据必定有值）    「平台限制」
                 * - RULE-1 = value,  RULE-3 = null（有可能，未和第三方同步，仅入库了）
                 *
                 * 2. 新数据
                 *
                 * - RULE-1 = null                             （第三方数据必定没有code）
                 * - 如果是UCMDB的新数据，RULE-2 必须满足         UCMDB 中不可能 globalId 为空
                 * - 如果是第三方的新数据，RULE-3 必须满足         DDI 中的 IP 地址必须合法，也必须包含该数据
                 *
                 * 3. 更新记录
                 *
                 * 不推送检查，record 是推送专用规则，旧记录不满足推送规则，那么使用
                 * 旧记录在 priority 中按序查找数据，看是否可以找到，如果可以找到，则
                 * 旧记录直接更新不推送。
                 */
                final JsonObject draft = Ux.ruleAny(rule.rulePure(), queueNew, recordO);
                if (Objects.nonNull(draft)) {
                    /*
                     *「小概率」
                     * 按照标识规则优先级检索，根据旧数据在新队列中查找到了相关数据
                     * 此种情况下，RULE-1中的CMDB可能出现了碰撞，即：
                     *
                     * - 旧数据：系统中的草稿数据，同时不满足 RULE-3
                     * - 新数据：巧合，可通过该规则查找到
                     */
                    queueUTemp.add(draft);
                } else {
                    Ao.infoDiff(AoCompare.class, "旧数据不满足推送规则，并且无法在新队列中找到！{0}", recordO.encode());
                }
            } else {
                /*
                 * 旧数据只剩
                 * 1 x 3 - 无globalId，「TP」业务标识规则已满足，但推送失败（异常数据）
                 * 1 2 3 - 有globalId，「TP」数据已经对齐了
                 *
                 * 2）检查旧记录是否具有强规则
                 *
                 * 旧数据满足的情况：
                 * 1）RULE-1/RULE-3 = value, RULE-2 = null
                 * 2）RULE-1/RULE-3 = value, RULE-2 = value
                 *
                 * 推送检查，record 是推送专用规则，旧记录已经满足了推送规则，旧记录是否满足了优先级（按顺序查找）
                 */
                final JsonObject foundO = Ux.ruleAll(rule.rulePull(), recordO);
                if (Objects.isNull(foundO)) {
                    /*
                     * （未推送）, RULE-2 = null 的情况
                     *
                     * 可推送、游离状态（不执行删除）
                     * x v v  - v x v , v v v
                     * 旧记录为 x v v，没有集成过，如果在新记录中按 code 或 ip 匹配上
                     * 则直接进入添加队列
                     *
                     * 这种情况非常特殊，旧记录按照优先级查询无法查询到相关信息，证明数据有污染
                     * CMDB 内部存储的数据中出现了旧的 x v v 格式的数据记录，这样的情况下直接
                     * 在 JsonArray 的新记录中查询 updated 队列，看是否可以查找到
                     *
                     * - 可以找到：做更新，新记录直接覆盖旧记录数据
                     * - 不可以找到，旧记录直接保留，生成游离记录「游离态」
                     *
                     *
                     * 新数据
                     * 1）RULE-1 无值，RULE-3 必定有值（正常情况）
                     * 2）RULE-1 有值，RULE-3 必定有值（异常情况）
                     */
                    final JsonObject found = Ux.ruleAny(rule.rulePush(), queueNew, recordO);
                    if (Objects.nonNull(found)) {
                        
                    }
                }
            }
        });
        return null;
    }
}
