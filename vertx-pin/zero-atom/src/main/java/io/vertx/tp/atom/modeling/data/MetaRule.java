package io.vertx.tp.atom.modeling.data;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.commune.rule.RuleUnique;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * 标识规则专用
 */
class MetaRule {
    private transient final Model modelRef;
    /*
     * Running Rule
     * 运行过程中通道内的标识规则信息
     */
    private transient RuleUnique rule;

    MetaRule(final Model modelRef) {
        /* 模型引用信息 */
        this.modelRef = modelRef;
    }

    /*
     * 直接返回模型中存储的标识规则
     */
    RuleUnique rule() {
        return this.modelRef.getUnique();
    }

    /*
     * 直接返回当前模型连接的标识规则（第二标识规则）
     */
    RuleUnique ruleDirect() {
        return this.rule;
    }

    /*
     * 1）先检索连接的标识规则：Slave
     * 2）再检索存储的标识规则：Master
     */
    RuleUnique ruleSmart() {
        if (Objects.nonNull(this.rule)) {
            return this.rule;
        } else {
            return this.modelRef.getUnique();
        }
    }

    void connect(final RuleUnique channelRule) {
        this.rule = channelRule;
    }
}
