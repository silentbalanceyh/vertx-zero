package io.mature.extension.migration.modeling;

import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/*
 * 修正专用
 * - MEntity
 * - MModel
 * - MField
 * - MAttribute
 */
public interface Revision {

    static Revision get(final Class<?> daoCls) {
        Revision revision = Pool.POOL.get(daoCls);
        if (Objects.isNull(revision)) {
            revision = Ut.singleton(EmptyRevision.class);
            Ox.LOG.Atom.warn(daoCls, "选择的 Revision: {0}", revision.getClass().getName());
        } else {
            Ox.LOG.Atom.info(daoCls, "选择的 Revision: {0}", revision.getClass().getName());
        }
        return revision;
    }

    /*
     * 读取 Revision 中的 metadata 计算结果
     * 1） 按表区分
     * 2） 输入：
     *     identifier = key 或 name = key
     * 3） 输出：
     *     key = metadata
     */
    Future<ConcurrentMap<String, JsonObject>> captureAsync(ConcurrentMap<String, String> keyMap);
}
