package io.vertx.tp.plugin.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.exception.zero.JooqClassInvalidException;
import io.vertx.up.exception.zero.JooqVertxNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Table;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Container to wrap Jooq / VertxDAO
 * 1. Jooq DSL will support sync operation
 * 2. VertxDAO support the async operation
 *
 * The instance counter is as following:
 *
 * 1. 1 vertx instance
 * 2. 1 configuration instance ( 1 context instance )
 * 3. n clazz = Dao instances
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class JooqDsl {
    private static final Annal LOGGER = Annal.get(JooqDsl.class);
    private static final ConcurrentMap<String, JooqDsl> DSL_MAP = new ConcurrentHashMap<>();
    private transient Vertx vertxRef;
    private transient Configuration configurationRef;
    private transient String poolKey;

    private transient JooqMeta metadata;
    private transient JooqDao daoSync;
    private transient VertxDAO dao;

    private JooqDsl(final String poolKey) {
        this.poolKey = poolKey;
    }

    static JooqDsl init(final Vertx vertxRef, final Configuration configurationRef, final Class<?> daoCls) {
        // Checking when initializing
        Fn.out(!Ut.isImplement(daoCls, VertxDAO.class), JooqClassInvalidException.class, JooqDsl.class, daoCls.getName());
        Fn.outUp(null == vertxRef, LOGGER, JooqVertxNullException.class, daoCls);

        // Calculate the key of current pool
        final String poolKey = String.valueOf(vertxRef.hashCode()) + ":" +
            String.valueOf(configurationRef.hashCode()) + ":" + daoCls.getName();
        return Fn.pool(DSL_MAP, poolKey, () -> new JooqDsl(poolKey).bind(vertxRef, configurationRef).store(daoCls));
    }

    private JooqDsl bind(final Vertx vertxRef, final Configuration configurationRef) {
        this.vertxRef = vertxRef;
        this.configurationRef = configurationRef;
        return this;
    }

    private <T> JooqDsl store(final Class<?> daoCls) {
        /*
         * VertxDao initializing
         * Old:
         * final T dao = Ut.instance(clazz, configuration);
         * Ut.invoke(dao, "setVertx", vertxRef);
         */
        final VertxDAO vertxDAO = Ut.instance(daoCls, configurationRef, vertxRef);
        this.dao = vertxDAO;
        this.metadata = JooqMeta.create(vertxDAO, daoCls);
        this.daoSync = new JooqDao(this.metadata, this.configurationRef);
        return this;
    }

    public String poolKey() {
        return this.poolKey;
    }

    // ----------------------- Metadata
    public Table<?> getTable() {
        return this.metadata.table();
    }

    public Class<?> getType() {
        return this.metadata.type();
    }

    public DSLContext context() {
        return this.daoSync.context();
    }

    // ----------------------- Sync/Async Read Operation
    public <T> List<T> fetchAll() {
        return this.daoSync.fetchAll();
    }

    public <T> List<T> fetch(final Condition condition) {
        return this.daoSync.fetch(condition);
    }

    public <T, ID> T fetchById(ID id) {
        return this.daoSync.fetchById(id);
    }

    public <T> T fetchOne(final Condition condition) {
        return this.daoSync.fetchOne(condition);
    }

    public <T> Future<List<T>> fetchAllAsync() {
        return (Future<List<T>>) dao.findAll();
    }

    public <T> Future<List<T>> fetchAsync(final Condition condition) {
        return (Future<List<T>>) dao.findManyByCondition(condition);
    }

    public <T, ID> Future<T> fetchByIdAsync(ID id) {
        return (Future<T>) dao.findOneById(id);
    }

    public <T> Future<T> fetchOneAsync(final Condition condition) {
        return (Future<T>) dao.findOneByCondition(condition);
    }

    // ----------------------- Internal Call
    public <T, ID> boolean existById(ID id) {
        return Objects.nonNull(this.fetchById(id));
    }

    public <T, ID> Future<Boolean> existByIdAsync(ID id) {
        return this.fetchByIdAsync(id)
            .compose(queried -> Future.succeededFuture(Objects.nonNull(queried)));
    }

    public Long count() {
        return Long.valueOf(this.fetchAll().size());
    }

    public Future<Long> countAsync() {
        return this.fetchAllAsync()
            .compose(qlist -> Future.succeededFuture(Long.valueOf(qlist.size())));
    }

    public Long count(final Condition condition) {
        return Long.valueOf(this.fetch(condition).size());
    }

    public Future<Long> countAsync(final Condition condition) {
        return this.fetchAsync(condition)
            .compose(qlist -> Future.succeededFuture(Long.valueOf(qlist.size())));
    }

    // ----------------------- Sync/Async Write Operation
    public <T> T insert(final T pojo) {
        return this.daoSync.insert(pojo);
    }

    public <T> List<T> insert(final List<T> pojo) {
        return this.daoSync.insert(pojo);
    }

    public <T> Future<T> insertAsync(final T pojo) {
        return (Future<T>) this.dao.insert(pojo);
    }

    public <T> Future<List<T>> insertAsync(final List<T> pojo) {
        return (Future<List<T>>) this.dao.insert(pojo);
    }

    public <T> T update(final T pojo) {
        return this.daoSync.update(pojo);
    }

    public <T> List<T> update(final List<T> pojo) {
        return this.daoSync.update(pojo);
    }

    public <T> Future<T> updateAsync(final T pojo) {
        return (Future<T>) this.dao.update(pojo);
    }

    public <T> Future<List<T>> updateAsync(final List<T> pojo) {
        return null;
    }

    public <T> T delete(final T pojo) {
        return this.daoSync.delete(pojo);
    }

    public <ID> Boolean deleteById(final Collection<ID> id) {
        return this.daoSync.deleteByIds(id);
    }

    public <T> List<T> delete(final List<T> pojo) {
        return this.daoSync.delete(pojo);
    }

    public <T> Future<T> deleteAsync(final T pojo) {
        return null;
    }

    public <T> Future<List<T>> deleteAsync(final List<T> pojo) {
        return null;
    }

    public <T, ID> Future<Boolean> deleteByIdAsync(final Collection<ID> ids) {
        return ((Future) this.dao.deleteByIds(ids))
            .compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }
}
