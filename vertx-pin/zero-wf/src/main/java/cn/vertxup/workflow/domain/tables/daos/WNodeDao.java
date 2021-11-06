/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.workflow.domain.tables.daos;


import cn.vertxup.workflow.domain.tables.WNode;
import cn.vertxup.workflow.domain.tables.records.WNodeRecord;

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
public class WNodeDao extends AbstractVertxDAO<WNodeRecord, cn.vertxup.workflow.domain.tables.pojos.WNode, String, Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>>, Future<cn.vertxup.workflow.domain.tables.pojos.WNode>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<WNodeRecord,cn.vertxup.workflow.domain.tables.pojos.WNode,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public WNodeDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(WNode.W_NODE, cn.vertxup.workflow.domain.tables.pojos.WNode.class, new JDBCClassicQueryExecutor<WNodeRecord,cn.vertxup.workflow.domain.tables.pojos.WNode,String>(configuration,cn.vertxup.workflow.domain.tables.pojos.WNode.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.workflow.domain.tables.pojos.WNode object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByName(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCode(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByType(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>FLOW_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFlowId(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.FLOW_ID.in(values));
        }

        /**
     * Find records that have <code>FLOW_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFlowId(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.FLOW_ID.in(values),limit);
        }

        /**
     * Find records that have <code>FORM_CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFormCode(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.FORM_CODE.in(values));
        }

        /**
     * Find records that have <code>FORM_CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFormCode(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.FORM_CODE.in(values),limit);
        }

        /**
     * Find records that have <code>FORM_RULE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFormRule(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.FORM_RULE.in(values));
        }

        /**
     * Find records that have <code>FORM_RULE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByFormRule(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.FORM_RULE.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByModelId(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.MODEL_ID.in(values));
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByModelId(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.MODEL_ID.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByModelKey(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.MODEL_KEY.in(values));
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByModelKey(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.MODEL_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>ENTITY_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByEntityId(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.ENTITY_ID.in(values));
        }

        /**
     * Find records that have <code>ENTITY_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByEntityId(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.ENTITY_ID.in(values),limit);
        }

        /**
     * Find records that have <code>ENTITY_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByEntityKey(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.ENTITY_KEY.in(values));
        }

        /**
     * Find records that have <code>ENTITY_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByEntityKey(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.ENTITY_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>RULE_ROLE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleRole(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.RULE_ROLE.in(values));
        }

        /**
     * Find records that have <code>RULE_ROLE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleRole(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.RULE_ROLE.in(values),limit);
        }

        /**
     * Find records that have <code>RULE_GROUP IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleGroup(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.RULE_GROUP.in(values));
        }

        /**
     * Find records that have <code>RULE_GROUP IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleGroup(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.RULE_GROUP.in(values),limit);
        }

        /**
     * Find records that have <code>RULE_USER IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleUser(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.RULE_USER.in(values));
        }

        /**
     * Find records that have <code>RULE_USER IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByRuleUser(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.RULE_USER.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(WNode.W_NODE.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(WNode.W_NODE.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(WNode.W_NODE.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(WNode.W_NODE.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(WNode.W_NODE.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(WNode.W_NODE.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(WNode.W_NODE.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.workflow.domain.tables.pojos.WNode>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(WNode.W_NODE.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<WNodeRecord,cn.vertxup.workflow.domain.tables.pojos.WNode,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<WNodeRecord,cn.vertxup.workflow.domain.tables.pojos.WNode,String>) super.queryExecutor();
        }
}