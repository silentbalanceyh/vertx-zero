/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.daos;


import cn.vertxup.rbac.domain.tables.SPermSet;
import cn.vertxup.rbac.domain.tables.records.SPermSetRecord;

import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractVertxDAO;

import java.time.LocalDateTime;
import java.util.Collection;

import org.jooq.Configuration;


import java.util.List;
import io.vertx.core.Future;
import io.github.jklingsporn.vertx.jooq.classic.jdbc.JDBCClassicQueryExecutor;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SPermSetDao extends AbstractVertxDAO<SPermSetRecord, cn.vertxup.rbac.domain.tables.pojos.SPermSet, String, Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>>, Future<cn.vertxup.rbac.domain.tables.pojos.SPermSet>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<SPermSetRecord,cn.vertxup.rbac.domain.tables.pojos.SPermSet,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public SPermSetDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(SPermSet.S_PERM_SET, cn.vertxup.rbac.domain.tables.pojos.SPermSet.class, new JDBCClassicQueryExecutor<SPermSetRecord,cn.vertxup.rbac.domain.tables.pojos.SPermSet,String>(configuration,cn.vertxup.rbac.domain.tables.pojos.SPermSet.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.rbac.domain.tables.pojos.SPermSet object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByName(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCode(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByType(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>COMMENT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByComment(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.COMMENT.in(values));
        }

        /**
     * Find records that have <code>COMMENT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByComment(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.COMMENT.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(SPermSet.S_PERM_SET.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SPermSet>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(SPermSet.S_PERM_SET.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<SPermSetRecord,cn.vertxup.rbac.domain.tables.pojos.SPermSet,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<SPermSetRecord,cn.vertxup.rbac.domain.tables.pojos.SPermSet,String>) super.queryExecutor();
        }
}
