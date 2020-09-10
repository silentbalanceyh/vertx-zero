package io.vertx.tp.modular.ray;

import io.vertx.tp.atom.modeling.element.DataTpl;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * 引用专用解析器，在所有读取结束过后解析引用信息
 * 引用包含：
 * 1. REFERENCE
 * 2. EXTERNAL
 */
public interface AoRay<T> {

    AoRay<T> on(DataTpl tpl);

    T attach(T input);
}
