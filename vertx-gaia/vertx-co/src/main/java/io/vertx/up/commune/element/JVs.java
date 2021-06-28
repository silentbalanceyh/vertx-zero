package io.vertx.up.commune.element;

import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;

/**
 * ## 属性解析器
 *
 * ### Intro
 *
 * 1. "rule"节点中的"diff"：处理数组的标识规则字段
 * 2. "fields"节点：既照顾UI，又包含类型
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JVs implements Serializable {
    private transient final String name;
    private final transient Set<String> diffSet = new HashSet<>();
    private final transient List<JTypeItem> shapes = new ArrayList<>();

    public JVs(final String name) {
        this.name = name;
    }

    public JVs bind(final JsonArray diffArray) {
        return this.bind(Ut.toSet(diffArray));
    }

    public JVs bind(final Set<String> diffSet) {
        if (Objects.nonNull(diffSet)) {
            this.diffSet.addAll(diffSet);
        }
        return this;
    }

    public JVs bind(final List<JTypeItem> items) {
        this.shapes.addAll(items);
        return this;
    }

    public Set<String> fieldDiff() {
        return this.diffSet;
    }
}
