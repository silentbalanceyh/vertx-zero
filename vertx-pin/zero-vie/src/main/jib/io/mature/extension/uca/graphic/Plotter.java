package io.mature.extension.uca.graphic;

import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Set;

/*
 * 绘图仪
 */
public interface Plotter {

    Plotter bind(HArk app);

    /*
     * 初始化图专用
     */
    Future<JsonObject> drawAsync(String recordId, String relationId);

    Future<JsonObject> drawAsync(String recordId, String relationId, Set<String> ignore);
}
