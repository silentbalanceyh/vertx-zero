package io.mature.extension.uca.log;

import cn.vertxup.ambient.domain.tables.pojos.XLog;
import io.mature.extension.cv.em.TypeLog;
import io.mature.extension.refine.Ox;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

class KoData {

    static XLog database(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = KoTool.error(clazz, atom, ex);
        log.setType(TypeLog.DATABASE_ERROR.name());
        return log;
    }

    static XLog integration(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = KoTool.error(clazz, atom, ex);
        log.setType(TypeLog.INTEGRATION_ERROR.name());
        log.setInfoReadable(Ox.Env.message(TypeLog.INTEGRATION_ERROR));
        return log;
    }

    static XLog ongoing(final Class<?> clazz, final DataAtom atom, final JsonObject record) {
        final XLog log = KoTool.warn(clazz, atom, record);
        log.setType(TypeLog.TODO_ONGOING.name());
        log.setInfoSystem(toMessage(record));
        log.setInfoReadable(Ox.Env.message(TypeLog.TODO_ONGOING));
        return log;
    }

    static XLog pending(final Class<?> clazz, final DataAtom atom, final JsonObject data) {
        final XLog log = KoTool.info(clazz, atom, data);
        log.setType(TypeLog.PUSH_NONE.name());
        log.setInfoSystem(toMessage(data));
        log.setInfoReadable(Ox.Env.message(TypeLog.PUSH_NONE));
        return log;
    }

    static XLog push(final Class<?> clazz, final DataAtom atom, final JsonObject data) {
        final XLog log = KoTool.warn(clazz, atom, data);
        log.setType(TypeLog.PUSH_FAIL.name());
        log.setInfoSystem(toMessage(data));
        log.setInfoReadable(Ox.Env.message(TypeLog.PUSH_FAIL));
        return log;
    }

    static XLog push(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = KoTool.error(clazz, atom, ex);
        log.setType(TypeLog.PUSH_BATCH_FAIL.name());
        return log;
    }

    static List<XLog> ongoing(final Class<?> clazz, final DataAtom atom, final JsonArray records) {
        final List<XLog> logs = new ArrayList<>();
        Ut.itJArray(records).map(each -> ongoing(clazz, atom, each)).forEach(logs::add);
        return logs;
    }

    static List<XLog> unique(final Class<?> clazz, final DataAtom atom, final JsonArray data) {
        final List<XLog> logs = new ArrayList<>();
        Ut.itJArray(data).map(each -> unique(clazz, atom, each)).forEach(logs::add);
        return logs;
    }

    static List<XLog> pending(final Class<?> clazz, final DataAtom atom, final JsonArray data) {
        final List<XLog> logs = new ArrayList<>();
        Ut.itJArray(data).map(each -> pending(clazz, atom, each)).forEach(logs::add);
        return logs;
    }

    static XLog unique(final Class<?> clazz, final DataAtom atom, final JsonObject data) {
        final XLog log = KoTool.info(clazz, atom, data);
        /*
         * 信息部分
         */
        final String pattern = "访问模型：identifier = {0}, 标识规则未满足：data = {1}";
        log.setInfoReadable(MessageFormat.format(pattern, atom.identifier(), data.encode()));
        log.setType(TypeLog.INVALID_DATA.name());
        return log;
    }

    private static String toMessage(final JsonObject record) {
        final JsonObject recordJson = Ut.valueJObject(record);
        return MessageFormat.format("数据记录：{0}", recordJson.encodePrettily());
    }
}
