package cn.originx.uca.code;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * 序号管理器
 */
public interface Numeration {
    /* 批量序号生成，ADD, DELETE, UPDATE */
    Future<ConcurrentMap<ChangeFlag, Queue<String>>> numberAsync(
        Class<?> clazz, ConcurrentMap<ChangeFlag, JsonArray> dataMap);

    /* 批量序号生成 */
    Future<Queue<String>> numberAsync(Class<?> clazz, Integer counter);

    /* 序号生成 */
    Future<String> numberAsync(Class<?> clazz);

    /* 序号生成专用（按 identifier 生成序号）*/
    Future<String> numberByIdAsync(String identifier);

    Future<Queue<String>> numberByIdAsync(String identifier, Integer counter);

    /* 序号生成专用（直接根据 code）*/
    Future<String> numberAsync(String code);
}

interface NumerationMap {

    ConcurrentMap<Class<?>, String> NUM_MAP = new ConcurrentHashMap<Class<?>, String>() {
        {
            this.put(XActivity.class, "NUM.ACTIVITY");
            this.put(XTodo.class, "NUM.TODO");
        }
    };
}
