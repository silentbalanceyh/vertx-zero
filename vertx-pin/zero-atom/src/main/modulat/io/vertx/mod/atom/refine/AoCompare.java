package io.vertx.mod.atom.refine;

import io.horizon.atom.common.Kv;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.compare.Vs;
import io.modello.specification.atom.HRule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.atom.refine.Ao.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AoCompare {
    private AoCompare() {
    }

    static <T> ConcurrentMap<ChangeFlag, List<T>> initMList() {
        return new ConcurrentHashMap<ChangeFlag, List<T>>() {
            {
                this.put(ChangeFlag.DELETE, new ArrayList<>());
                this.put(ChangeFlag.ADD, new ArrayList<>());
                this.put(ChangeFlag.UPDATE, new ArrayList<>());
            }
        };
    }

    static <T> ConcurrentMap<ChangeFlag, Queue<T>> initMQueue() {
        return new ConcurrentHashMap<ChangeFlag, Queue<T>>() {
            {
                this.put(ChangeFlag.DELETE, new ConcurrentLinkedQueue<>());
                this.put(ChangeFlag.ADD, new ConcurrentLinkedQueue<>());
                this.put(ChangeFlag.UPDATE, new ConcurrentLinkedQueue<>());
            }
        };
    }

    static ConcurrentMap<ChangeFlag, JsonArray> initMArray() {
        return new ConcurrentHashMap<ChangeFlag, JsonArray>() {
            {
                this.put(ChangeFlag.DELETE, new JsonArray());
                this.put(ChangeFlag.ADD, new JsonArray());
                this.put(ChangeFlag.UPDATE, new JsonArray());
            }
        };
    }

    /*
     * Database Change
     */
    static ConcurrentMap<ChangeFlag, JsonArray> diffPure(
        final JsonArray queueO, final JsonArray queueN,
        final DataAtom atom, final Set<String> ignoreSet
    ) {
        final HRule rules = atom.ruleSmart();
        LOG.Uca.info(AoCompare.class, "（Pure）对比用标识规则：\n{0}", rules.toString());
        final JsonArray queueA = new JsonArray();
        final JsonArray queueUTemp = new JsonArray();
        final JsonArray queueD = new JsonArray();
        // DELETE / UPDATE
        Ut.itJArray(queueO).forEach(recordO -> {
            final JsonObject found = Ux.ruleAny(rules.rulePure(), queueN, recordO);
            if (Objects.isNull(found)) {
                // DELETE
                queueD.add(Ux.ruleTwins(recordO, null));
            } else {
                queueUTemp.add(found);
            }
        });
        // ADD
        Ut.itJArray(queueN).forEach(recordN -> {
            final JsonObject found = Ux.ruleAny(rules.rulePure(), queueO, recordN);
            if (Objects.isNull(found)) {
                queueA.add(Ux.ruleTwins(null, recordN));
            }
        });
        final JsonArray queueU = updateQueue(queueUTemp, atom, ignoreSet);
        return new ConcurrentHashMap<ChangeFlag, JsonArray>() {
            {
                this.put(ChangeFlag.ADD, queueA);
                this.put(ChangeFlag.DELETE, queueD);
                this.put(ChangeFlag.UPDATE, queueU);
            }
        };
    }

    static JsonObject diffPure(
        final JsonObject recordO, final JsonObject recordN,
        final DataAtom atom, final Set<String> ignoreSet
    ) {
        final JsonArray queueN = new JsonArray().add(recordN);
        final JsonArray queueO = new JsonArray().add(recordO);
        final ConcurrentMap<ChangeFlag, JsonArray> processed = diffPure(queueO, queueN, atom, ignoreSet);
        final Kv<ChangeFlag, JsonObject> changed = updateKv(processed);
        final ChangeFlag flag = changed.key();
        LOG.Uca.info(AoCompare.class, "（Pure）计算变更结果：{0} = {1}", flag, changed.value());
        if (ChangeFlag.UPDATE == flag) {
            return recordN;
        } else {
            return null;
        }
    }

    static Apt diffPure(final Apt apt, final DataAtom atom,
                        final Set<String> ignoreSet) {
        final JsonArray queueO = apt.dataO();
        final JsonArray queueN = apt.dataN();
        LOG.Uca.info(AoCompare.class, "（Pure）新旧数量：（{2} vs {3}），新数据：{1}, 旧数据：{0}",
            queueO.encode(), queueN.encode(),
            String.valueOf(queueN.size()), String.valueOf(queueO.size()));
        /*
         * 变更历史的判断，是否生成变更历史
         */
        final ConcurrentMap<ChangeFlag, JsonArray> calculated = diffPure(queueO, queueN, atom, ignoreSet);
        return apt.compared(calculated);
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
        final HRule rules = atom.ruleSmart();
        LOG.Diff.info(AoCompare.class, "（Pull）对比用标识规则：\n{0}", rules.toString());
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
            final JsonObject twinO = Ux.ruleAll(rules.rulePush(), recordO);
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
                final JsonObject draft = Ux.ruleAny(rules.rulePure(), queueNew, recordO);
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
                    LOG.Diff.info(AoCompare.class, "旧数据不满足推送规则，并且无法在新队列中找到！{0}", recordO.encode());
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
                final JsonObject branch = Ux.ruleAll(rules.rulePure(), recordO);
                if (Objects.isNull(branch)) {
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
                    // 有序查找，List带优先级模式
                    final JsonObject found = Ux.ruleAny(rules.rulePush(), queueNew, recordO);
                    if (Objects.nonNull(found)) {
                        /*
                         * 当前数据是匹配的
                         * RULE-1, RULE-3 任意有一条规则是匹配的
                         *
                         * 1）如果 code 存在，则按 code 创建连接
                         * 2）如果 code 不存在，则按 name 第三规则创建连接
                         * UPDATE 添加（合并添加）
                         * 一条新记录覆盖旧记录中的可关联的基础数据
                         *
                         * 限制：如果出现了 name 匹配上，code 匹配不上（提供了code）-- ！！！超小概率处理
                         *
                         * Solution：
                         * 在集成的 mapping 中控制 code，最好不配置
                         */
                        final JsonObject foundAny = Ux.ruleAny(new HashSet<>(rules.rulePush()), queueNew, recordO);
                        if (Objects.isNull(foundAny)) {
                            /*
                             * 新数据：只有 name，没有 code
                             * 更新平台内的 code 数据
                             */
                            queueUTemp.add(found);
                        } else {
                            /*
                             * 新数据：有 name， 有 code
                             * 忽略：超小概率，直接忽略
                             */
                            LOG.Diff.info(AoCompare.class, "第三方提供了 code，数据异常：{0}", recordO.encode());
                        }
                    }
                } else {
                    /*
                     * （已推送），RULE-2 = value 的情况
                     *
                     * 旧数据：标识规则全满足
                     * 新数据：
                     * 1）RULE-3 - value，RULE-2/RULE-1 = value
                     * 2）RULE-3 - value, RULE-2 = value, RULE-1 = null;
                     * 3）RULE-3 - value, RULE-2 = null, RULE-1 = value;
                     *
                     * UCMDB为源头：RULE-2
                     * 情况1：推送过一次的更新
                     * 情况2：自动发现的更新（不太容易发生）
                     * 情况3：不会发生，RULE-2 必须存在
                     *
                     *
                     * 可推送、同步状态（可执行删除）
                     * v v v - v x v, v v v
                     * Strong有一个连接上就标识为可连接
                     *
                     * 旧记录满足了 priority 的标识规则（合法）
                     */
                    final JsonObject foundStrong = Ux.ruleAny(rules.ruleStrong(), queueNew, recordO);
                    if (Objects.isNull(foundStrong)) {
                        /*
                         * 新数据
                         * DDI为源头：RULE-3
                         * 情况1：不可能
                         * 情况2：不可能
                         * 情况3：不可能
                         * RULE-1: null, RULE-2: null
                         *
                         */

                        /*
                         * 强连接失效、那么执行弱连接操作
                         */
                        final JsonObject foundWeak = Ux.ruleAny(rules.ruleWeak(), queueNew, recordO);
                        if (Objects.isNull(foundWeak)) {
                            /*
                             * 弱连接未匹配，直接删除
                             */
                            queueD.add(Ux.ruleTwins(recordO, null));
                        } else {
                            /*
                             * 强连接失效，弱连接生效，那么需要针对新旧数据执行替换
                             * 强连接字段不执行替换（按弱连接不更新强连接）
                             */
                            final JsonObject newRef = foundWeak.getJsonObject(KName.__.NEW);
                            final JsonObject oldRef = foundWeak.getJsonObject(KName.__.OLD);
                            rules.ruleStrong().forEach(rule -> rule.getFields().forEach(field -> {
                                /*
                                 * 以旧数据中的强连接字段为主
                                 */
                                final Object oldV = oldRef.getValue(field);
                                final Object newV = newRef.getValue(field);
                                if (Objects.nonNull(oldV) || Objects.nonNull(newV)) {
                                    /*
                                     * 新 = null，旧 = value
                                     * 新 = value, 旧 = null
                                     * 新 = value1, 旧 = value2
                                     * 最终旧值维持不变，而新的值Strong中的（Strong和Weak互斥）全部按旧值处理
                                     */
                                    newRef.put(field, oldV);
                                }
                            }));
                            /*
                             * 更新过后的内容
                             */
                            queueUTemp.add(foundWeak);
                        }
                    } else {
                        /*
                         * 强连接生效：旧记录直接通过强连接连接上了，证明该记录可执行更新合并操作，整体生效
                         */
                        queueUTemp.add(foundStrong);
                    }
                }
            }
        });
        /*
         * 遍历剩余的 updated，生成纯添加队列：ADD
         * 新记录 newRecord / oldRecord 旧队列
         * v v v
         * v x v
         * 旧记录状态：x v x, x v v, v v v
         */
        Ut.itJArray(queueNew).map(recordN -> {
            final JsonObject processed = Ux.ruleAny(rules.rulePure(), queueOld, recordN);
            if (Objects.isNull(processed)) {
                return Ux.ruleTwins(null, recordN);
            } else {
                final JsonObject weakFound = Ux.ruleAny(rules.ruleWeak(), queueOld, recordN);
                if (Objects.isNull(weakFound)) {
                    /*
                     * 如果弱连接找不到
                     * 不添加
                     */
                    return null;
                } else {
                    /*
                     * 反向（新旧交换）
                     */
                    final JsonObject revert = new JsonObject();
                    revert.put(KName.__.NEW, weakFound.getValue(KName.__.OLD));
                    revert.put(KName.__.OLD, weakFound.getValue(KName.__.NEW));
                    if (queueD.contains(revert)) {
                        /*
                         * 弱连接可以找到，但原始的被删除了，添加新的
                         */
                        revert.remove(KName.__.OLD);
                        return revert;
                    } else {
                        /*
                         * 弱连接可以找到，但原始的并未被删除，不添加新的
                         */
                        return null;
                    }
                }
            }
        }).filter(Objects::nonNull).forEach(queueA::add);

        final JsonArray queueU = updateQueue(queueUTemp, atom, ignoreSet);
        return new ConcurrentHashMap<ChangeFlag, JsonArray>() {
            {
                this.put(ChangeFlag.ADD, queueA);
                this.put(ChangeFlag.DELETE, queueD);
                this.put(ChangeFlag.UPDATE, queueU);
            }
        };
    }

    static JsonObject diffPull(
        final JsonObject recordO, final JsonObject recordN,
        final DataAtom atom, final Set<String> ignoreSet
    ) {
        final JsonArray queueN = new JsonArray().add(recordN);
        final JsonArray queueO = new JsonArray().add(recordO);
        final ConcurrentMap<ChangeFlag, JsonArray> processed = diffPull(queueO, queueN, atom, ignoreSet);
        final Kv<ChangeFlag, JsonObject> changed = updateKv(processed);
        final ChangeFlag flag = changed.key();
        LOG.Uca.info(AoCompare.class, "（Pull）计算变更结果：{0} = {1}", flag, changed.value());
        if (ChangeFlag.UPDATE == flag) {
            return recordN;
        } else {
            return null;
        }
    }

    static Apt diffPull(final Apt apt, final DataAtom atom,
                        final Set<String> ignoreSet) {
        final JsonArray queueO = apt.dataO();
        final JsonArray queueN = apt.dataN();
        LOG.Uca.info(AoCompare.class, "（Pull）新旧数量：（{2} vs {3}），新数据：{1}, 旧数据：{0}",
            queueO.encode(), queueN.encode(),
            String.valueOf(queueN.size()), String.valueOf(queueO.size()));
        /*
         * 变更历史的判断，是否生成变更历史
         */
        final ConcurrentMap<ChangeFlag, JsonArray> calculated = diffPull(queueO, queueN, atom, ignoreSet);
        return apt.compared(calculated);
    }

    private static Kv<ChangeFlag, JsonObject> updateKv(final ConcurrentMap<ChangeFlag, JsonArray> processed) {
        final Kv<ChangeFlag, JsonObject> changed = Kv.create();
        processed.forEach((flag, items) -> {
            if (VValue.ONE == items.size()) {
                changed.set(flag, items.getJsonObject(VValue.IDX));
            }
        });
        return changed;
    }

    private static JsonArray updateQueue(final JsonArray queueUTemp, final DataAtom atom, final Set<String> ignoreSet) {
        /*
         * updateQueue 压缩：如果没有变更则不处理
         */
        final JsonArray queueU = new JsonArray();
        /*
         * ATOM-02: ignore 中还包含 track = false 的字段
         */
        final Vs vs = atom.vs().ignores(ignoreSet);
        Ut.itJArray(queueUTemp).filter(vs::isChange).forEach(queueU::add);
        return queueU;
    }
}
