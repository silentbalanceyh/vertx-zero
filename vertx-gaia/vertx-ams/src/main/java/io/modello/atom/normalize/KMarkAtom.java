package io.modello.atom.normalize;

import io.modello.eon.em.Marker;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 模型的属性标记集，保存了当前模型的所有属性标记，用于针对属性执行不同操作。
 * 标记结构在特殊场景中使用，调用时不使用固定方法，而是采用标记位传入的方式来执行相关处理
 * 实现标记的动态扩展，最终的扩展步骤：
 * <pre><code>
 *     1. 直接修改 VAtom.Mark 中的标记信息，添加新的标记位
 *     2. 新版中 {@link KMarkAttribute} 和 {@link KMarkAtom} 都不需要更改
 * </code></pre>
 *
 * 特定场景使用标记比直接使用属性的模式要方便，新版充当容器作用，则实现了标记位的动态扩展。
 * 更改时只需重写 {@link Marker} 枚举类型即可追加新标记
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KMarkAtom {

    /**
     * 是否开启当前 Atom 模型的追踪功能，追踪功能包含优先级：
     * 1. 模型级的追踪功能主要用于定义模型是否产生追踪日志
     * 2. 属性级的追踪功能用于生成变更日志、操作日志等
     * 属性级的追踪依赖模型级的追踪功能是打开的，trackable = true 的模型的属性 track 才生效。
     * 否则所有生成日志的功能在这种场景下都是无效的。
     * 且 trackable 属性是不可以更改的，只能在构造时确定。
     */
    private final boolean trackable;
    private final ConcurrentMap<String, KMarkAttribute> markMap = new ConcurrentHashMap<>();

    private KMarkAtom(final Boolean trackable) {
        this.trackable = trackable;
    }

    public static KMarkAtom of(final Boolean trackable) {
        return new KMarkAtom(Optional.ofNullable(trackable).orElse(Boolean.TRUE));
    }

    public boolean trackable() {
        return this.trackable;
    }

    // -------------------- 追加和更改 ---------------------
    public void put(final String name, final KMarkAttribute mark) {
        this.markMap.put(name, mark);
    }

    public void put(final String name, final String literal) {
        this.markMap.put(name, KMarkAttribute.of(literal));
    }

    public KMarkAttribute get(final String name) {
        return this.markMap.getOrDefault(name, KMarkAttribute.of());
    }
    // -------------------- 属性提取 ---------------------

    public Set<String> enabled(final Marker mark) {
        return this.connect(markAttr -> markAttr.value(mark), Boolean.TRUE);
    }

    public Set<String> disabled(final Marker mark) {
        return this.connect(markAttr -> markAttr.value(mark), Boolean.FALSE);
    }

    private Set<String> connect(final Function<KMarkAttribute, Boolean> function, final Boolean defaultV) {
        final Set<String> set = new HashSet<>();
        this.markMap.keySet().forEach(field -> {
            final KMarkAttribute tag = this.markMap.get(field);
            if (Objects.nonNull(tag)) {
                final Boolean result = function.apply(tag);
                if (Objects.nonNull(result) && defaultV.booleanValue() == result.booleanValue()) {
                    // Skip all NULL value
                    set.add(field);
                }
            }
        });
        return set;
    }
}
