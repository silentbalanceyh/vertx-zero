package io.macrocosm.specification.program;

import io.horizon.eon.VName;
import io.horizon.eon.VString;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.secure.HoI;
import io.modello.atom.app.KDS;
import io.modello.atom.app.KDatabase;

import java.util.Objects;
import java.util.function.Function;

/**
 * 「方舟」Ark
 * <hr/>
 * 运行实例的容器对象，和应用执行一对一的绑定关系，之中会包含信息如下：
 * <pre><code>
 *     1. 应用信息
 *     2. 租户信息
 *     3. 所有数据源信息
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HArk extends Function<HArk, HArk> {

    HApp app();

    /**
     * 应用配置容器中的数据库定义
     *
     * @param <T> {@link KDatabase} 的子类
     *
     * @return {@link KDS} 数据库定义
     */
    <T extends KDatabase> KDS<T> database();

    /**
     * 当前应用所属的拥有者信息
     *
     * @return {@link HoI} 拥有者
     */
    default HoI owner() {
        return null;
    }

    @Override
    default HArk apply(final HArk app) {
        return this;
    }

    // 高频属性部分：----------------------------------------------------------

    /**
     * 当前容器的维度信息，字符串类型，在不同场景使用方法有所区别
     *
     * @return 维度信息
     */
    default String sigma() {
        return this.app().option(VName.SIGMA);
    }

    /**
     * 当前容器的语言信息，字符串类型，在不同场景使用方法有所区别
     *
     * @return 语言信息
     */
    default String language() {
        return this.app().option(VName.LANGUAGE);
    }

    /**
     * 根据传入的 identifier 提供专用缓存键
     *
     * @param identifier 传入的 identifier
     *
     * @return 缓存键
     */
    default String cached(final String identifier) {
        Objects.requireNonNull(identifier);
        return this.app().ns() + VString.SLASH + identifier;
    }
}
