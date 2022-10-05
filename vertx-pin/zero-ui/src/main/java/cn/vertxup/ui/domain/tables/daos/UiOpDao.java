/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.daos;


import cn.vertxup.ui.domain.tables.UiOp;
import cn.vertxup.ui.domain.tables.records.UiOpRecord;

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
public class UiOpDao extends AbstractVertxDAO<UiOpRecord, cn.vertxup.ui.domain.tables.pojos.UiOp, String, Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>>, Future<cn.vertxup.ui.domain.tables.pojos.UiOp>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<UiOpRecord,cn.vertxup.ui.domain.tables.pojos.UiOp,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     * @param vertx the vertx instance
     */
        public UiOpDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(UiOp.UI_OP, cn.vertxup.ui.domain.tables.pojos.UiOp.class, new JDBCClassicQueryExecutor<UiOpRecord,cn.vertxup.ui.domain.tables.pojos.UiOp,String>(configuration,cn.vertxup.ui.domain.tables.pojos.UiOp.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.ui.domain.tables.pojos.UiOp object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>ACTION IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByAction(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.ACTION.in(values));
        }

        /**
     * Find records that have <code>ACTION IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByAction(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.ACTION.in(values),limit);
        }

        /**
     * Find records that have <code>TEXT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByText(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.TEXT.in(values));
        }

        /**
     * Find records that have <code>TEXT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByText(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.TEXT.in(values),limit);
        }

        /**
     * Find records that have <code>EVENT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByEvent(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.EVENT.in(values));
        }

        /**
     * Find records that have <code>EVENT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByEvent(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.EVENT.in(values),limit);
        }

        /**
     * Find records that have <code>CLIENT_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByClientKey(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CLIENT_KEY.in(values));
        }

        /**
     * Find records that have <code>CLIENT_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByClientKey(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CLIENT_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>CLIENT_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByClientId(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CLIENT_ID.in(values));
        }

        /**
     * Find records that have <code>CLIENT_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByClientId(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CLIENT_ID.in(values),limit);
        }

        /**
     * Find records that have <code>CONFIG IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByConfig(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CONFIG.in(values));
        }

        /**
     * Find records that have <code>CONFIG IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByConfig(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CONFIG.in(values),limit);
        }

        /**
     * Find records that have <code>PLUGIN IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByPlugin(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.PLUGIN.in(values));
        }

        /**
     * Find records that have <code>PLUGIN IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByPlugin(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.PLUGIN.in(values),limit);
        }

        /**
     * Find records that have <code>CONTROL_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByControlId(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CONTROL_ID.in(values));
        }

        /**
     * Find records that have <code>CONTROL_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByControlId(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CONTROL_ID.in(values),limit);
        }

        /**
     * Find records that have <code>CONTROL_TYPE IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByControlType(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CONTROL_TYPE.in(values));
        }

        /**
     * Find records that have <code>CONTROL_TYPE IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByControlType(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CONTROL_TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(UiOp.UI_OP.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(UiOp.UI_OP.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(UiOp.UI_OP.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(UiOp.UI_OP.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ui.domain.tables.pojos.UiOp>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(UiOp.UI_OP.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<UiOpRecord,cn.vertxup.ui.domain.tables.pojos.UiOp,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<UiOpRecord,cn.vertxup.ui.domain.tables.pojos.UiOp,String>) super.queryExecutor();
        }
}
