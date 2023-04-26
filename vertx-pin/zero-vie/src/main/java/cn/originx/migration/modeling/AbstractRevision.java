package cn.originx.migration.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cn.originx.refine.Ox.LOG;

/*
 * 直接读取
 */
public abstract class AbstractRevision implements Revision {
    private transient final Set<String> edition = new HashSet<>();
    private transient final Set<String> deletion = new HashSet<>();
    private transient final Set<String> visible = new HashSet<>();
    private transient final Set<String> relation = new HashSet<>();

    protected AbstractRevision(final Class<?> clazz) {
        final String simpleName = clazz.getSimpleName();
        /*
         * 读取配置文件生成
         * edition
         * deletion
         * visible
         */
        final JsonObject config = Ao.adjuster(simpleName);
        this.edition.addAll(Ut.toSet(config.getJsonArray("edition")));
        this.deletion.addAll(Ut.toSet(config.getJsonArray("deletion")));
        this.visible.addAll(Ut.toSet(config.getJsonArray("visible")));
        this.relation.addAll(Ut.toSet(config.getJsonArray("relation")));
    }

    @Override
    public Future<ConcurrentMap<String, JsonObject>> captureAsync(final ConcurrentMap<String, String> keyMap) {
        final ConcurrentMap<String, JsonObject> resultMap = new ConcurrentHashMap<>();
        LOG.Shell.info(this.getClass(), "修正总数量：{0}", String.valueOf(keyMap.size()));
        keyMap.forEach((key, field) -> {
            /*
             * Build JsonObject
             */
            final JsonObject metadata = new JsonObject();
            metadata.put("edition", !this.edition.contains(field));
            metadata.put("deletion", !this.deletion.contains(field));
            metadata.put("visible", !this.visible.contains(field));
            metadata.put("relation", !this.relation.contains(field));
            LOG.Shell.info(this.getClass(), "修正数据：{0}, metadata: {1}", field, metadata.encode());
            resultMap.put(key, metadata);
        });
        return Ux.future(resultMap);
    }
}
