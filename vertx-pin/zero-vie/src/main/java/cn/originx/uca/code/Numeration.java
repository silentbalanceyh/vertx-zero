package cn.originx.uca.code;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

/*
 * 序号管理器
 */
public interface Numeration {

    static Numeration service(final String sigma) {
        return Fn.pool(NumerationService.POOL_SERVICE, sigma, () -> new NumerationService(sigma));
    }

    static void preprocess(final Class<?> clazz, final String code) {
        SeqClass.bindStatic(clazz, code);
    }

    static void preprocess(final ConcurrentMap<Class<?>, String> map) {
        map.forEach(SeqClass::bindStatic);
    }

    /* 批量序号生成，ADD, DELETE, UPDATE */
    Future<ConcurrentMap<ChangeFlag, Queue<String>>> clazz(Class<?> clazz, ConcurrentMap<ChangeFlag, JsonArray> dataMap);

    /* 序号生成：Dao 模式 */
    default Future<String> clazz(final Class<?> clazz) {
        return this.clazz(clazz, new JsonObject());
    }

    default Future<String> clazz(final Class<?> clazz, final JsonObject options) {
        return this.clazz(clazz, 1, options).compose(queue -> Ux.future(queue.poll()));
    }

    default Future<Queue<String>> clazz(final Class<?> clazz, final Integer counter) {
        return this.clazz(clazz, counter, new JsonObject());
    }

    Future<Queue<String>> clazz(Class<?> clazz, Integer counter, JsonObject options);

    /* 序号生成专用（按 identifier 生成序号）*/
    default Future<String> atom(final DataAtom atom) {
        return this.atom(atom, new JsonObject());
    }

    default Future<String> atom(final DataAtom atom, final JsonObject options) {
        return this.atom(atom, 1, options).compose(queue -> Ux.future(queue.poll()));
    }

    default Future<Queue<String>> atom(final DataAtom atom, final Integer counter) {
        return this.atom(atom, counter, new JsonObject());
    }

    Future<Queue<String>> atom(DataAtom atom, Integer counter, JsonObject options);

    /* 序号生成专用（直接根据 code）*/
    default Future<String> indent(final String code) {
        return this.indent(code, new JsonObject());
    }

    default Future<String> indent(final String code, final JsonObject options) {
        return this.indent(code, 1, options).compose(queue -> Ux.future(queue.poll()));
    }

    default Future<Queue<String>> indent(final String code, final Integer counter) {
        return this.indent(code, counter, new JsonObject());
    }

    Future<Queue<String>> indent(final String code, final Integer counter, final JsonObject options);
}
