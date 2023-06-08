package io.modello.specification.action;

import io.horizon.uca.compare.Vs;

/**
 * 模型比对器专用方法，现阶段版本直接返回比对器 {@link Vs}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HDiff {

    Vs vs();
}
