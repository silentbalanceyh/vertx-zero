/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.psi.domain.tables.daos;


import cn.vertxup.psi.domain.tables.PAmountSpec;
import cn.vertxup.psi.domain.tables.records.PAmountSpecRecord;

import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractVertxDAO;

import java.math.BigDecimal;
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
public class PAmountSpecDao extends AbstractVertxDAO<PAmountSpecRecord, cn.vertxup.psi.domain.tables.pojos.PAmountSpec, String, Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>>, Future<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<PAmountSpecRecord,cn.vertxup.psi.domain.tables.pojos.PAmountSpec,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public PAmountSpecDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(PAmountSpec.P_AMOUNT_SPEC, cn.vertxup.psi.domain.tables.pojos.PAmountSpec.class, new JDBCClassicQueryExecutor<PAmountSpecRecord,cn.vertxup.psi.domain.tables.pojos.PAmountSpec,String>(configuration,cn.vertxup.psi.domain.tables.pojos.PAmountSpec.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.psi.domain.tables.pojos.PAmountSpec object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyBySerial(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.SERIAL.in(values));
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyBySerial(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.SERIAL.in(values),limit);
        }

        /**
     * Find records that have <code>COMMODITY_ID IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityId(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_ID.in(values));
        }

        /**
     * Find records that have <code>COMMODITY_ID IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityId(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_ID.in(values),limit);
        }

        /**
     * Find records that have <code>COMMODITY_CODE IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityCode(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_CODE.in(values));
        }

        /**
     * Find records that have <code>COMMODITY_CODE IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityCode(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_CODE.in(values),limit);
        }

        /**
     * Find records that have <code>COMMODITY_NAME IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityName(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_NAME.in(values));
        }

        /**
     * Find records that have <code>COMMODITY_NAME IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCommodityName(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.COMMODITY_NAME.in(values),limit);
        }

        /**
     * Find records that have <code>WH_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByWhId(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.WH_ID.in(values));
        }

        /**
     * Find records that have <code>WH_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByWhId(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.WH_ID.in(values),limit);
        }

        /**
     * Find records that have <code>AMOUNT_MIN IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByAmountMin(Collection<BigDecimal> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.AMOUNT_MIN.in(values));
        }

        /**
     * Find records that have <code>AMOUNT_MIN IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByAmountMin(Collection<BigDecimal> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.AMOUNT_MIN.in(values),limit);
        }

        /**
     * Find records that have <code>AMOUNT_MAX IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByAmountMax(Collection<BigDecimal> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.AMOUNT_MAX.in(values));
        }

        /**
     * Find records that have <code>AMOUNT_MAX IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByAmountMax(Collection<BigDecimal> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.AMOUNT_MAX.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.psi.domain.tables.pojos.PAmountSpec>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(PAmountSpec.P_AMOUNT_SPEC.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<PAmountSpecRecord,cn.vertxup.psi.domain.tables.pojos.PAmountSpec,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<PAmountSpecRecord,cn.vertxup.psi.domain.tables.pojos.PAmountSpec,String>) super.queryExecutor();
        }
}
