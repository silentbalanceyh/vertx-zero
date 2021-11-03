/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.daos;


import cn.vertxup.ambient.domain.tables.XAttachment;
import cn.vertxup.ambient.domain.tables.records.XAttachmentRecord;

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
public class XAttachmentDao extends AbstractVertxDAO<XAttachmentRecord, cn.vertxup.ambient.domain.tables.pojos.XAttachment, String, Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>>, Future<cn.vertxup.ambient.domain.tables.pojos.XAttachment>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<XAttachmentRecord,cn.vertxup.ambient.domain.tables.pojos.XAttachment,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public XAttachmentDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(XAttachment.X_ATTACHMENT, cn.vertxup.ambient.domain.tables.pojos.XAttachment.class, new JDBCClassicQueryExecutor<XAttachmentRecord,cn.vertxup.ambient.domain.tables.pojos.XAttachment,String>(configuration,cn.vertxup.ambient.domain.tables.pojos.XAttachment.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.ambient.domain.tables.pojos.XAttachment object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByName(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>EXTENSION IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByExtension(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.EXTENSION.in(values));
        }

        /**
     * Find records that have <code>EXTENSION IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByExtension(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.EXTENSION.in(values),limit);
        }

        /**
     * Find records that have <code>MODULE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByModule(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.MODULE.in(values));
        }

        /**
     * Find records that have <code>MODULE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByModule(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.MODULE.in(values),limit);
        }

        /**
     * Find records that have <code>MIME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByMime(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.MIME.in(values));
        }

        /**
     * Find records that have <code>MIME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByMime(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.MIME.in(values),limit);
        }

        /**
     * Find records that have <code>SIZE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyBySize(Collection<Integer> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.SIZE.in(values));
        }

        /**
     * Find records that have <code>SIZE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyBySize(Collection<Integer> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.SIZE.in(values),limit);
        }

        /**
     * Find records that have <code>STATUS IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStatus(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STATUS.in(values));
        }

        /**
     * Find records that have <code>STATUS IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStatus(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STATUS.in(values),limit);
        }

        /**
     * Find records that have <code>STORE_WAY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStoreWay(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STORE_WAY.in(values));
        }

        /**
     * Find records that have <code>STORE_WAY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStoreWay(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STORE_WAY.in(values),limit);
        }

        /**
     * Find records that have <code>STORE_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStoreId(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STORE_ID.in(values));
        }

        /**
     * Find records that have <code>STORE_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByStoreId(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.STORE_ID.in(values),limit);
        }

        /**
     * Find records that have <code>FILE_NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileName(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_NAME.in(values));
        }

        /**
     * Find records that have <code>FILE_NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileName(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_NAME.in(values),limit);
        }

        /**
     * Find records that have <code>FILE_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileKey(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_KEY.in(values));
        }

        /**
     * Find records that have <code>FILE_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileKey(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>FILE_URL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileUrl(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_URL.in(values));
        }

        /**
     * Find records that have <code>FILE_URL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFileUrl(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_URL.in(values),limit);
        }

        /**
     * Find records that have <code>FILE_PATH IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFilePath(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_PATH.in(values));
        }

        /**
     * Find records that have <code>FILE_PATH IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByFilePath(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.FILE_PATH.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XAttachment>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XAttachment.X_ATTACHMENT.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<XAttachmentRecord,cn.vertxup.ambient.domain.tables.pojos.XAttachment,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<XAttachmentRecord,cn.vertxup.ambient.domain.tables.pojos.XAttachment,String>) super.queryExecutor();
        }
}
