/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.atom.domain.tables.daos;


import cn.vertxup.atom.domain.tables.MIndex;
import cn.vertxup.atom.domain.tables.records.MIndexRecord;

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
public class MIndexDao extends AbstractVertxDAO<MIndexRecord, cn.vertxup.atom.domain.tables.pojos.MIndex, String, Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>>, Future<cn.vertxup.atom.domain.tables.pojos.MIndex>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<MIndexRecord,cn.vertxup.atom.domain.tables.pojos.MIndex,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public MIndexDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(MIndex.M_INDEX, cn.vertxup.atom.domain.tables.pojos.MIndex.class, new JDBCClassicQueryExecutor<MIndexRecord,cn.vertxup.atom.domain.tables.pojos.MIndex,String>(configuration,cn.vertxup.atom.domain.tables.pojos.MIndex.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.atom.domain.tables.pojos.MIndex object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByName(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByType(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>CLUSTERED IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByClustered(Collection<Boolean> values) {
                return findManyByCondition(MIndex.M_INDEX.CLUSTERED.in(values));
        }

        /**
     * Find records that have <code>CLUSTERED IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByClustered(Collection<Boolean> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.CLUSTERED.in(values),limit);
        }

        /**
     * Find records that have <code>COLUMNS IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByColumns(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.COLUMNS.in(values));
        }

        /**
     * Find records that have <code>COLUMNS IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByColumns(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.COLUMNS.in(values),limit);
        }

        /**
     * Find records that have <code>ENTITY_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByEntityId(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.ENTITY_ID.in(values));
        }

        /**
     * Find records that have <code>ENTITY_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByEntityId(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.ENTITY_ID.in(values),limit);
        }

        /**
     * Find records that have <code>COMMENTS IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByComments(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.COMMENTS.in(values));
        }

        /**
     * Find records that have <code>COMMENTS IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByComments(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.COMMENTS.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(MIndex.M_INDEX.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MIndex.M_INDEX.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MIndex.M_INDEX.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(MIndex.M_INDEX.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MIndex>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MIndex.M_INDEX.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<MIndexRecord,cn.vertxup.atom.domain.tables.pojos.MIndex,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<MIndexRecord,cn.vertxup.atom.domain.tables.pojos.MIndex,String>) super.queryExecutor();
        }
}
