/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.rbac.domain.tables.daos;


import cn.vertxup.rbac.domain.tables.SRole;
import cn.vertxup.rbac.domain.tables.records.SRoleRecord;
import io.github.jklingsporn.vertx.jooq.classic.jdbc.JDBCClassicQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractVertxDAO;
import io.vertx.core.Future;
import org.jooq.Configuration;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SRoleDao extends AbstractVertxDAO<SRoleRecord, cn.vertxup.rbac.domain.tables.pojos.SRole, String, Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>>, Future<cn.vertxup.rbac.domain.tables.pojos.SRole>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<SRoleRecord,cn.vertxup.rbac.domain.tables.pojos.SRole,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     * @param vertx the vertx instance
     */
        public SRoleDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(SRole.S_ROLE, cn.vertxup.rbac.domain.tables.pojos.SRole.class, new JDBCClassicQueryExecutor<SRoleRecord,cn.vertxup.rbac.domain.tables.pojos.SRole,String>(configuration,cn.vertxup.rbac.domain.tables.pojos.SRole.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.rbac.domain.tables.pojos.SRole object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByName(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCode(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>POWER IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByPower(Collection<Boolean> values) {
                return findManyByCondition(SRole.S_ROLE.POWER.in(values));
        }

        /**
     * Find records that have <code>POWER IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByPower(Collection<Boolean> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.POWER.in(values),limit);
        }

        /**
     * Find records that have <code>COMMENT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByComment(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.COMMENT.in(values));
        }

        /**
     * Find records that have <code>COMMENT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByComment(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.COMMENT.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByModelId(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.MODEL_ID.in(values));
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByModelId(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.MODEL_ID.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByModelKey(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.MODEL_KEY.in(values));
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByModelKey(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.MODEL_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>CATEGORY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCategory(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.CATEGORY.in(values));
        }

        /**
     * Find records that have <code>CATEGORY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCategory(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.CATEGORY.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(SRole.S_ROLE.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(SRole.S_ROLE.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(SRole.S_ROLE.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(SRole.S_ROLE.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.rbac.domain.tables.pojos.SRole>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(SRole.S_ROLE.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<SRoleRecord,cn.vertxup.rbac.domain.tables.pojos.SRole,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<SRoleRecord,cn.vertxup.rbac.domain.tables.pojos.SRole,String>) super.queryExecutor();
        }
}
