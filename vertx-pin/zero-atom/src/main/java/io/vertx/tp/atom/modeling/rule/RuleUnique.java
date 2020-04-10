package io.vertx.tp.atom.modeling.rule;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/*
 * 表示规则专用模型，用于绑定 X_MODEL 中的 ruleUnique 专用
 * 数据库中的状态
 * 1）草稿：不可推送数据（可能是用户填充，也可能是第三方集成导致）
 * 2）合法：可推送，一旦推送就可以实现 ucmdb_id 的回调（如果有四个规则，那么合法中同样包含了最小规则）
 * 3）完整：同步过一次的数据记录，包含完整的 标识规则数据
 *      说明：    ucmdb_id  |  code  |   ip
 *      草稿：           x  |     v  |    x
 *      合法：           x  |     v  |    v
 *      完整：           v  |     v  |    v
 * 连接规则处理：
 *      本库 - 集成拉取
 *                 x v x   |   v v v   - （稀有，草稿态拉取的数据不可能有 code）
 *                 x v v   |   v v v   - （稀有，合法态是一个瞬间状态，回写过后消失）
 *                 v v v   |   v v v
 *                 x v x   |   v x v
 *                 x v v   |   v x v
 *                 v v v   |   v x v   - （稀有，同步过的数据除非 UCMDB 中删除，否则一定会有 code ）
 * 目标说明：
 * 1）第三方集成说明：
 * - 第三方集成只同步数据到 CMDB，CMDB 不反写数据
 * - 第三方集成不生成草稿（也就是必须满足合法性规则）
 * - 第三方集成失败的情况，直接在系统中生成集成专用日志
 *
 * 2）人工保存说明：
 * - 人工保存可执行多次，可生成草稿
 * - 人工保存后，只有合法才会触发到 UCMDB 的集成流程，推送数据
 * - 有两种数据可以推送到 UCMDB，合法的、完整的
 * - 集成过后回调会反向写 ucmdb_id / code 相关信息（参考连接 规则2 和 规则3 ）
 *
 * 3）UCMDB 拉取说明：
 * - 默认从 UCMDB 中拉取的数据有两种状态 （ 带code和不带code ）
 * - 注意稀有状态的说明，正常创建连接最终只有三种情况
 * - 3.1） x v x | v v v - 草稿状态下的拉取
 *   （禁止）草稿状态下，无法匹配，直接在 配置平台 创建新记录，任何情况下都无法创建连接
 *
 * - 3.2） x v v | v v v - 这种情况下，UCMDB中的数据为旧数据，碰撞概率比较小
 *   3.2.1 - 第二位置绝对不相等，生成的 code 和 UCMDB 拉取的 code 铁定不等
 *   3.2.2 - 第三位置可能相等（二者ip是匹配的）
 *           1）如果 ip 是强连接，则直接合并，反正库中的数据没有同步，处于游离状态
 *           2）如果 ip 是弱连接，（无法创建）只记录日志信息，创建不会成功，因为系统已存在【日志】
 *
 * - 3.3） v v v | v v v - 最正常情况的连接
 *   3.3.1 - 按优先级匹配强连接，匹配到任何一条强连接规则，那么以优先级最高的规则执行更新
 *   3.3.2 - 如果强连接没匹配上，遇上了弱连接匹配，那么直接忽略【日志】
 *
 * - 3.4） x v x | v x v - 正常情况，未保存的拉取
 *   （禁止）草稿状态下，无法匹配成功，直接创建新记录，丢失草稿
 *
 * - 3.5） x v v | v x v - 合法数据
 *   第三位置不论是否强弱，都反向写入到配置项数据，并且合并 code
 *
 * - 3.6） v v v | v x v - 非正常匹配
 */
public class RuleUnique implements Serializable {
    /*
     * （无优先级）可推送的规则：
     * 1）草稿 -> 合法状态的标准规则；
     * 2）这种规则下，可将数据推送到 UCMDB 并且实现 ucmdb_id 的回写
     * ---
     * 读取第三方源使用该规则
     * 合法规则，无优先级，只要满足则可入库，不满足规则则不可入库
     * 带优先级，在游离态创建连接需要根据优先级创建
     */
    private transient List<RuleTerm> record;
    /*
     * （无优先级）可接受规则：
     * 1）集成可入记录规则，从 UCMDB 中读取数据专用
     * 2）可接受无 code 这种数据信息
     * ---
     * 读取 UCMDB 中数据的专用规则，可以没有 code
     */
    private transient Set<RuleTerm> integration;
    /*
     * 带优先级的标识规则，识别专用
     */
    private transient List<RuleTerm> priority;
    /*
     * 强连接
     */
    private transient Set<RuleTerm> strong;
    /*
     * 弱连接
     */
    private transient Set<RuleTerm> weak;

    public List<RuleTerm> getRecord() {
        return this.record;
    }

    public void setRecord(final List<RuleTerm> record) {
        this.record = record;
    }

    public Set<RuleTerm> getIntegration() {
        return this.integration;
    }

    public void setIntegration(final Set<RuleTerm> integration) {
        this.integration = integration;
    }

    public List<RuleTerm> getPriority() {
        return this.priority;
    }

    public void setPriority(final List<RuleTerm> priority) {
        this.priority = priority;
    }

    public Set<RuleTerm> getStrong() {
        return this.strong;
    }

    public void setStrong(final Set<RuleTerm> strong) {
        this.strong = strong;
    }

    public Set<RuleTerm> getWeak() {
        return this.weak;
    }

    public void setWeak(final Set<RuleTerm> weak) {
        this.weak = weak;
    }

    @Override
    public String toString() {
        return "RuleUnique{" +
                "\n\trecord=" + this.record +
                ",\n\tintegration=" + this.integration +
                ",\n\tpriority=" + this.priority +
                ",\n\tstrong=" + this.strong +
                ",\n\tweak=" + this.weak +
                "\n}";
    }
}
