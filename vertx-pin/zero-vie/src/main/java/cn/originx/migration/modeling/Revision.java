package cn.originx.migration.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import static cn.originx.refine.Ox.LOG;

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
            LOG.Atom.warn(daoCls, "选择的 Revision: {0}", revision.getClass().getName());
        } else {
            LOG.Atom.info(daoCls, "选择的 Revision: {0}", revision.getClass().getName());
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
