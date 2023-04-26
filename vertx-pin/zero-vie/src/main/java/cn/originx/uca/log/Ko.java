package cn.originx.uca.log;

import cn.vertxup.ambient.domain.tables.pojos.XLog;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;

import java.util.List;

import static cn.originx.refine.Ox.LOG;

/*
 * 日志记录器，连接 XLog
 */
public class Ko {
    // ------------------- 单数据同步日志 ---------------------
    /*
     * （ERROR）- 数据库访问日志
     * DATABASE_ACCESS
     */
    public static XLog db(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = KoData.database(clazz, atom, ex);
        LOG.Hub.warn(Ko.class, "数据库异常：{0}", ex.getMessage());
        return KoTool.log(log);
    }

    public static XLog integration(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = KoData.integration(clazz, atom, ex);
        LOG.Hub.warn(Ko.class, "集成异常：{0}", ex.getMessage());
        return KoTool.log(log);
    }

    public static List<XLog> ongoing(final Class<?> clazz, final DataAtom atom, final JsonArray records) {
        final List<XLog> logs = KoData.ongoing(clazz, atom, records);
        LOG.Hub.warn(Ko.class, "忽略记录异常：size = {0}", String.valueOf(logs.size()));
        return KoTool.log(logs);
    }
    // ------------------- 单数据异步日志 ---------------------

    public static Future<XLog> pending(final Class<?> clazz, final DataAtom atom, final JsonObject input) {
        final XLog log = KoData.pending(clazz, atom, input);
        LOG.Hub.info(Ko.class, "数据未推送：{0}", input.encode());
        return KoTool.logAsync(log);
    }

    public static Future<XLog> push(final Class<?> clazz, final DataAtom atom, final JsonObject input, final Throwable ex) {
        final XLog log = KoData.push(clazz, atom, input);
        LOG.Hub.info(Ko.class, "推送失败：{0}，异常：{1}", input.encode(), ex.getMessage());
        log.setInfoStack(ex.getMessage());
        return KoTool.logAsync(log);
    }

    public static Future<XLog> push(final Class<?> clazz, final DataAtom atom, final JsonArray input, final Throwable ex) {
        final XLog log = KoData.push(clazz, atom, ex);
        LOG.Hub.info(Ko.class, "推送失败：{0}，数据量：{1}", ex.getMessage(), String.valueOf(input.size()));
        log.setInfoStack(ex.getMessage());
        return KoTool.logAsync(log);
    }

    // ------------------- 多数据日志 ---------------------
    public static Future<List<XLog>> pendingBatch(final Class<?> clazz, final DataAtom atom, final JsonArray data) {
        final List<XLog> logs = KoData.pending(clazz, atom, data);
        LOG.Hub.warn(Ko.class, "数据未推送：size = {0}", String.valueOf(logs.size()));
        return KoTool.logAsync(logs);
    }

    /*
     * （INFO） - 标识规则验证异常
     * INVALID_DATA
     */
    public static Future<List<XLog>> uniqueBatch(final Class<?> clazz, final DataAtom atom, final JsonArray data) {
        final List<XLog> logs = KoData.unique(clazz, atom, data);
        LOG.Hub.warn(Ko.class, "标识规则验证异常：size = {0}", String.valueOf(logs.size()));
        return KoTool.logAsync(logs);
    }
}
