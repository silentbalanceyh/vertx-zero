package io.vertx.mod.workflow.init;

import cn.vertxup.workflow.cv.WfCv;
import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class WfTodo {
    private static final ConcurrentMap<String, JsonObject> TODO_DEF =
        new ConcurrentHashMap<>();

    private WfTodo() {
    }

    /**
     * 遗留方法，用于读取旧版中：plugin/wf/todo/ 目录下的基本待确认相关配置，作为扩展配置对待
     * 待办信息的生成过程中，也可以根据此处的基础分类待办来处理对应内容
     */
    static void initLegacy() {
        if (TODO_DEF.isEmpty()) {
            final List<String> files = Ut.ioFiles(WfCv.FOLDER_TODO, VPath.SUFFIX.JSON);
            LOG.Init.info(WfTodo.class, "Wf Todo Files: {0}", files.size());
            files.forEach(file -> {
                final String path = WfCv.FOLDER_TODO + file;
                final JsonObject todoDef = Ut.ioJObject(path);
                final String key = file.replace(VString.DOT + VPath.SUFFIX.JSON, VString.EMPTY);
                TODO_DEF.put(key, todoDef);
            });
        }
    }

    /**
     * 返回对应类型的配置信息，直接传入分类（文件名），根据文件名提取待办基础配置
     *
     * @param type {@link java.lang.String} 传入的分类信息
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    static JsonObject getTodo(final String type) {
        final JsonObject todoDef = TODO_DEF.get(type);
        return Objects.isNull(todoDef) ? new JsonObject() : todoDef.copy();
    }
}
