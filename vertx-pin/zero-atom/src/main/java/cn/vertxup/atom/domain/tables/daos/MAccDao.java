/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.atom.domain.tables.daos;


import cn.vertxup.atom.domain.tables.MAcc;
import cn.vertxup.atom.domain.tables.records.MAccRecord;

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
public class MAccDao extends AbstractVertxDAO<MAccRecord, cn.vertxup.atom.domain.tables.pojos.MAcc, String, Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>>, Future<cn.vertxup.atom.domain.tables.pojos.MAcc>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<MAccRecord,cn.vertxup.atom.domain.tables.pojos.MAcc,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public MAccDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(MAcc.M_ACC, cn.vertxup.atom.domain.tables.pojos.MAcc.class, new JDBCClassicQueryExecutor<MAccRecord,cn.vertxup.atom.domain.tables.pojos.MAcc,String>(configuration,cn.vertxup.atom.domain.tables.pojos.MAcc.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.atom.domain.tables.pojos.MAcc object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByModelId(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.MODEL_ID.in(values));
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByModelId(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.MODEL_ID.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByModelKey(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.MODEL_KEY.in(values));
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByModelKey(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.MODEL_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>RECORD_JSON IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordJson(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.RECORD_JSON.in(values));
        }

        /**
     * Find records that have <code>RECORD_JSON IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordJson(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.RECORD_JSON.in(values),limit);
        }

        /**
     * Find records that have <code>RECORD_RAW IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordRaw(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.RECORD_RAW.in(values));
        }

        /**
     * Find records that have <code>RECORD_RAW IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordRaw(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.RECORD_RAW.in(values),limit);
        }

        /**
     * Find records that have <code>RECORD_UNIQUE IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordUnique(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.RECORD_UNIQUE.in(values));
        }

        /**
     * Find records that have <code>RECORD_UNIQUE IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByRecordUnique(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.RECORD_UNIQUE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(MAcc.M_ACC.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MAcc.M_ACC.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MAcc.M_ACC.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(MAcc.M_ACC.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.atom.domain.tables.pojos.MAcc>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MAcc.M_ACC.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<MAccRecord,cn.vertxup.atom.domain.tables.pojos.MAcc,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<MAccRecord,cn.vertxup.atom.domain.tables.pojos.MAcc,String>) super.queryExecutor();
        }
}
