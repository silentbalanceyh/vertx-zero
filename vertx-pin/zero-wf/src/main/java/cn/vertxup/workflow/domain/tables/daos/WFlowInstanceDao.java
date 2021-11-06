/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.workflow.domain.tables.daos;


import cn.vertxup.workflow.domain.tables.WFlowInstance;
import cn.vertxup.workflow.domain.tables.records.WFlowInstanceRecord;

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
public class WFlowInstanceDao extends AbstractVertxDAO<WFlowInstanceRecord, cn.vertxup.workflow.domain.tables.pojos.WFlowInstance, String, Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>>, Future<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<WFlowInstanceRecord,cn.vertxup.workflow.domain.tables.pojos.WFlowInstance,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public WFlowInstanceDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(WFlowInstance.W_FLOW_INSTANCE, cn.vertxup.workflow.domain.tables.pojos.WFlowInstance.class, new JDBCClassicQueryExecutor<WFlowInstanceRecord,cn.vertxup.workflow.domain.tables.pojos.WFlowInstance,String>(configuration,cn.vertxup.workflow.domain.tables.pojos.WFlowInstance.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.workflow.domain.tables.pojos.WFlowInstance object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCode(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyBySerial(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.SERIAL.in(values));
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyBySerial(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.SERIAL.in(values),limit);
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByName(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>PHASE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByPhase(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.PHASE.in(values));
        }

        /**
     * Find records that have <code>PHASE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByPhase(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.PHASE.in(values),limit);
        }

        /**
     * Find records that have <code>FLOW_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByFlowId(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.FLOW_ID.in(values));
        }

        /**
     * Find records that have <code>FLOW_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByFlowId(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.FLOW_ID.in(values),limit);
        }

        /**
     * Find records that have <code>USER_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUserId(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.USER_ID.in(values));
        }

        /**
     * Find records that have <code>USER_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUserId(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.USER_ID.in(values),limit);
        }

        /**
     * Find records that have <code>COORDINATE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCoordinate(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.COORDINATE.in(values));
        }

        /**
     * Find records that have <code>COORDINATE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCoordinate(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.COORDINATE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WFlowInstance>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(WFlowInstance.W_FLOW_INSTANCE.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<WFlowInstanceRecord,cn.vertxup.workflow.domain.tables.pojos.WFlowInstance,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<WFlowInstanceRecord,cn.vertxup.workflow.domain.tables.pojos.WFlowInstance,String>) super.queryExecutor();
        }
}