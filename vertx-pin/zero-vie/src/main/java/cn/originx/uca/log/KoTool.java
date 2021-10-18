package cn.originx.uca.log;

import cn.vertxup.ambient.domain.tables.daos.XLogDao;
import cn.vertxup.ambient.domain.tables.pojos.XLog;
import io.netty.handler.logging.LogLevel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class KoTool {

    static XLog error(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
        final XLog log = create(clazz, atom);
        log.setLevel(LogLevel.ERROR.name());
        log.setInfoStack(ex.getMessage());
        return log;
    }

    static XLog warn(final Class<?> clazz, final DataAtom atom, final JsonObject data) {
        return create(clazz, atom, data, LogLevel.WARN);
    }

    static XLog info(final Class<?> clazz, final DataAtom atom, final JsonObject data) {
        return create(clazz, atom, data, LogLevel.INFO);
    }

    private static XLog create(final Class<?> clazz, final DataAtom atom, final JsonObject data, final LogLevel logLevel) {
        final XLog log = create(clazz, atom);
        if (data.containsKey(KName.CREATED_BY)) {
            log.setCreatedBy(data.getString(KName.CREATED_BY));
            log.setLogUser(data.getString(KName.CREATED_BY));
        }
        log.setLevel(logLevel.name());
        return log;
    }

    static XLog log(final XLog log) {
        return Ux.Jooq.on(XLogDao.class).insert(log);
    }

    static List<XLog> log(final List<XLog> logs) {
        return Ux.Jooq.on(XLogDao.class).insert(logs);
    }

    static Future<XLog> logAsync(final XLog log) {
        return Ux.Jooq.on(XLogDao.class).insertAsync(log);
    }

    static Future<List<XLog>> logAsync(final List<XLog> logs) {
        return Ux.Jooq.on(XLogDao.class).insertAsync(logs);
    }

    private static XLog create(final Class<?> clazz, final DataAtom atom) {
        final XLog log = new XLog();
        log.setKey(UUID.randomUUID().toString());
        final LocalDateTime eventTime = LocalDateTime.now();
        log.setInfoAt(eventTime);
        log.setActive(Boolean.TRUE);
        log.setCreatedAt(eventTime);
        /*
         * 记录基本部分
         */
        log.setLogAgent(clazz.getName());
        if (Objects.nonNull(atom)) {
            final Model model = atom.model();
            if (Objects.nonNull(model)) {
                log.setSigma(model.dbModel().getSigma());
                log.setLanguage(model.dbModel().getLanguage());
            }
        }
        return log;
    }
}
