package io.vertx.tp.plugin.jooq;

import io.github.jklingsporn.vertx.jooq.classic.VertxDAO;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.List;

/**
 * Sync Operation instead of DAO directly
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
class JooqDao {

    private final transient JooqMeta metadata;
    private final transient VertxDAO dao;
    private final transient Configuration configuration;

    JooqDao(final JooqMeta metadata, final Configuration configuration) {
        this.metadata = metadata;
        this.configuration = configuration;
        this.dao = metadata.dao();
    }

    DSLContext context() {
        return this.configuration.dsl();
    }

    // Fetch
    <T> List<T> fetchAll() {
        return null;
    }

    <T> List<T> fetch(final Condition condition) {
        return null;
    }

    <T, ID> T fetchById(final ID id) {
        return null;
    }

    <T> T fetchOne(final Condition condition) {
        return null;
    }

    <T> T update(final T pojo) {
        return pojo;
    }

    <T> List<T> update(final List<T> pojo) {
        return pojo;
    }

    <T> T insert(final T pojo) {
        return pojo;
    }

    <T> List<T> insert(final List<T> pojo) {
        return pojo;
    }

    <T> T delete(final T pojo) {
        return pojo;
    }

    <T> List<T> delete(final List<T> pojo) {
        return pojo;
    }

    <ID> Boolean deleteByIds(final Collection<ID> id) {
        return null;
    }
}
