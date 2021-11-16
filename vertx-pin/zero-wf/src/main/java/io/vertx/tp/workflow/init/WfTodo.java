package io.vertx.tp.workflow.init;

import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## 「Init」AtTodo
 *
 * ### 1. Intro
 *
 * This class is for `X_TODO` template file definition that be related to `type` field. The filename is `type` field value such as `ADD_CI`, `CHANGE_CI` etc.
 *
 * The configuration folder is as following:
 *
 * ```shell
 * // <pre><code>
 *    plugin/ambient/todo/
 * // </code></pre>
 * ```
 *
 * ### 2. Specification
 *
 * Ths content of each todo definition file is as following:
 *
 * ```json
 * // <pre><code class="json">
 * {
 *     "name": "`${code} - ${name}，单号：${serial}`",
 *     "icon": "edit",
 *     "code": "`${serial}`",
 *     "todoUrl": "`/ambient/todo-view?tid=${key}&id=${modelKey}`"
 * }
 * // </code></pre>
 * ```
 *
 * |parameter|comments|
 * |---|:---|
 * |name|The expression that will build the title of each todo record.|
 * |icon|The `icon` that will be showed on Front-End app.|
 * |code|The todo serial that will be generated in `X_NUMBER`.|
 * |todoUrl|The expression will could be generated todo hyperlink on Front-End app.|
 *
 * > Here zero framework used `common-jexl3` to parse extension, please check they syntax on official web site. {@see http://commons.apache.org/proper/commons-jexl/index.html}
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class WfTodo {
    /**
     * The todo definition of hash map `type = Json` format that stored todo configuration data here.
     */
    private static final ConcurrentMap<String, JsonObject> TODO_DEF =
        new ConcurrentHashMap<>();

    /**
     * The private constructor to let current class be Util only.
     */
    private WfTodo() {
    }

    /**
     * 「Booting」This method will be called when zero container booting up.
     */
    static void init() {
        if (TODO_DEF.isEmpty()) {
            final List<String> files = Ut.ioFiles(WfCv.TODO_FOLDER, FileSuffix.JSON);
            Wf.Log.infoInit(WfTodo.class, "At Todo Files: {0}", files.size());
            files.forEach(file -> {
                final String path = WfCv.TODO_FOLDER + file;
                final JsonObject todoDef = Ut.ioJObject(path);
                final String key = file.replace(Strings.DOT + FileSuffix.JSON, Strings.EMPTY);
                TODO_DEF.put(key, todoDef);
            });
        }
    }

    /**
     * Return to configuration data that convert to {@link io.vertx.core.json.JsonObject} here by type.
     *
     * @param type {@link java.lang.String} The type value passed.
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    static JsonObject getTodo(final String type) {
        final JsonObject todoDef = TODO_DEF.get(type);
        return Objects.isNull(todoDef) ? new JsonObject() : todoDef.copy();
    }
}
