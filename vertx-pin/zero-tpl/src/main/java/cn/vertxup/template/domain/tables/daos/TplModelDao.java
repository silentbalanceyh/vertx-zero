/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.template.domain.tables.daos;


import cn.vertxup.template.domain.tables.TplModel;
import cn.vertxup.template.domain.tables.records.TplModelRecord;

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
public class TplModelDao extends AbstractVertxDAO<TplModelRecord, cn.vertxup.template.domain.tables.pojos.TplModel, String, Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>>, Future<cn.vertxup.template.domain.tables.pojos.TplModel>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<TplModelRecord,cn.vertxup.template.domain.tables.pojos.TplModel,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     * @param vertx the vertx instance
     */
        public TplModelDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(TplModel.TPL_MODEL, cn.vertxup.template.domain.tables.pojos.TplModel.class, new JDBCClassicQueryExecutor<TplModelRecord,cn.vertxup.template.domain.tables.pojos.TplModel,String>(configuration,cn.vertxup.template.domain.tables.pojos.TplModel.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.template.domain.tables.pojos.TplModel object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByName(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCode(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.CODE.in(values));
        }

        /**
     * Find records that have <code>CODE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCode(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.CODE.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByType(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_CATEGORY IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplCategory(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_CATEGORY.in(values));
        }

        /**
     * Find records that have <code>TPL_CATEGORY IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplCategory(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_CATEGORY.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_INTEGRATION IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplIntegration(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_INTEGRATION.in(values));
        }

        /**
     * Find records that have <code>TPL_INTEGRATION IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplIntegration(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_INTEGRATION.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_ACL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplAcl(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ACL.in(values));
        }

        /**
     * Find records that have <code>TPL_ACL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplAcl(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ACL.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_ACL_VISIT IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplAclVisit(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ACL_VISIT.in(values));
        }

        /**
     * Find records that have <code>TPL_ACL_VISIT IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplAclVisit(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ACL_VISIT.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_MODEL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplModel(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_MODEL_.in(values));
        }

        /**
     * Find records that have <code>TPL_MODEL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplModel(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_MODEL_.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_ENTITY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplEntity(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ENTITY.in(values));
        }

        /**
     * Find records that have <code>TPL_ENTITY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplEntity(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_ENTITY.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_API IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplApi(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_API.in(values));
        }

        /**
     * Find records that have <code>TPL_API IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplApi(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_API.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_JOB IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplJob(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_JOB.in(values));
        }

        /**
     * Find records that have <code>TPL_JOB IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplJob(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_JOB.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_UI IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUi(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI.in(values));
        }

        /**
     * Find records that have <code>TPL_UI IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUi(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_UI_LIST IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUiList(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI_LIST.in(values));
        }

        /**
     * Find records that have <code>TPL_UI_LIST IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUiList(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI_LIST.in(values),limit);
        }

        /**
     * Find records that have <code>TPL_UI_FORM IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUiForm(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI_FORM.in(values));
        }

        /**
     * Find records that have <code>TPL_UI_FORM IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByTplUiForm(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.TPL_UI_FORM.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(TplModel.TPL_MODEL.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(TplModel.TPL_MODEL.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(TplModel.TPL_MODEL.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(TplModel.TPL_MODEL.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.template.domain.tables.pojos.TplModel>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(TplModel.TPL_MODEL.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<TplModelRecord,cn.vertxup.template.domain.tables.pojos.TplModel,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<TplModelRecord,cn.vertxup.template.domain.tables.pojos.TplModel,String>) super.queryExecutor();
        }
}
