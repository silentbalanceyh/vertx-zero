/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.daos;


import cn.vertxup.ambient.domain.tables.XActivity;
import cn.vertxup.ambient.domain.tables.records.XActivityRecord;
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
public class XActivityDao extends AbstractVertxDAO<XActivityRecord, cn.vertxup.ambient.domain.tables.pojos.XActivity, String, Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>>, Future<cn.vertxup.ambient.domain.tables.pojos.XActivity>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<XActivityRecord,cn.vertxup.ambient.domain.tables.pojos.XActivity,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     * @param vertx the vertx instance
     */
        public XActivityDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(XActivity.X_ACTIVITY, cn.vertxup.ambient.domain.tables.pojos.XActivity.class, new JDBCClassicQueryExecutor<XActivityRecord,cn.vertxup.ambient.domain.tables.pojos.XActivity,String>(configuration,cn.vertxup.ambient.domain.tables.pojos.XActivity.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.ambient.domain.tables.pojos.XActivity object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByType(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyBySerial(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.SERIAL.in(values));
        }

        /**
     * Find records that have <code>SERIAL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyBySerial(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.SERIAL.in(values),limit);
        }

        /**
     * Find records that have <code>DESCRIPTION IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByDescription(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.DESCRIPTION.in(values));
        }

        /**
     * Find records that have <code>DESCRIPTION IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByDescription(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.DESCRIPTION.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelId(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_ID.in(values));
        }

        /**
     * Find records that have <code>MODEL_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelId(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_ID.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelKey(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_KEY.in(values));
        }

        /**
     * Find records that have <code>MODEL_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelKey(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>MODEL_CATEGORY IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelCategory(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_CATEGORY.in(values));
        }

        /**
     * Find records that have <code>MODEL_CATEGORY IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByModelCategory(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.MODEL_CATEGORY.in(values),limit);
        }

        /**
     * Find records that have <code>TASK_NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByTaskName(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.TASK_NAME.in(values));
        }

        /**
     * Find records that have <code>TASK_NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByTaskName(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.TASK_NAME.in(values),limit);
        }

        /**
     * Find records that have <code>TASK_SERIAL IN (values)</code>
     * asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByTaskSerial(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.TASK_SERIAL.in(values));
        }

        /**
     * Find records that have <code>TASK_SERIAL IN (values)</code>
     * asynchronously limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByTaskSerial(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.TASK_SERIAL.in(values),limit);
        }

        /**
     * Find records that have <code>RECORD_OLD IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByRecordOld(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.RECORD_OLD.in(values));
        }

        /**
     * Find records that have <code>RECORD_OLD IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByRecordOld(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.RECORD_OLD.in(values),limit);
        }

        /**
     * Find records that have <code>RECORD_NEW IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByRecordNew(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.RECORD_NEW.in(values));
        }

        /**
     * Find records that have <code>RECORD_NEW IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByRecordNew(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.RECORD_NEW.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(XActivity.X_ACTIVITY.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XActivity>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XActivity.X_ACTIVITY.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<XActivityRecord,cn.vertxup.ambient.domain.tables.pojos.XActivity,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<XActivityRecord,cn.vertxup.ambient.domain.tables.pojos.XActivity,String>) super.queryExecutor();
        }
}
