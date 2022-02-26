/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.integration.domain.tables.daos;


import cn.vertxup.integration.domain.tables.IMessageTpl;
import cn.vertxup.integration.domain.tables.records.IMessageTplRecord;

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
public class IMessageTplDao extends AbstractVertxDAO<IMessageTplRecord, cn.vertxup.integration.domain.tables.pojos.IMessageTpl, String, Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>>, Future<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<IMessageTplRecord,cn.vertxup.integration.domain.tables.pojos.IMessageTpl,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public IMessageTplDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(IMessageTpl.I_MESSAGE_TPL, cn.vertxup.integration.domain.tables.pojos.IMessageTpl.class, new JDBCClassicQueryExecutor<IMessageTplRecord,cn.vertxup.integration.domain.tables.pojos.IMessageTpl,String>(configuration,cn.vertxup.integration.domain.tables.pojos.IMessageTpl.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.integration.domain.tables.pojos.IMessageTpl object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByName(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCode(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByType(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>EXPR_SUBJECT IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprSubject(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_SUBJECT.in(values));
        }

        /**
     * Find records that have <code>EXPR_SUBJECT IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprSubject(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_SUBJECT.in(values),limit);
        }

        /**
     * Find records that have <code>EXPR_CONTENT IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprContent(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_CONTENT.in(values));
        }

        /**
     * Find records that have <code>EXPR_CONTENT IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprContent(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_CONTENT.in(values),limit);
        }

        /**
     * Find records that have <code>EXPR_COMPONENT IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprComponent(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_COMPONENT.in(values));
        }

        /**
     * Find records that have <code>EXPR_COMPONENT IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByExprComponent(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.EXPR_COMPONENT.in(values),limit);
        }

        /**
     * Find records that have <code>APP_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByAppId(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.APP_ID.in(values));
        }

        /**
     * Find records that have <code>APP_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByAppId(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.APP_ID.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.integration.domain.tables.pojos.IMessageTpl>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(IMessageTpl.I_MESSAGE_TPL.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<IMessageTplRecord,cn.vertxup.integration.domain.tables.pojos.IMessageTpl,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<IMessageTplRecord,cn.vertxup.integration.domain.tables.pojos.IMessageTpl,String>) super.queryExecutor();
        }
}
