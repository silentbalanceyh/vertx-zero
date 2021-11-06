/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.daos;


import cn.vertxup.ambient.domain.tables.XEmailServer;
import cn.vertxup.ambient.domain.tables.records.XEmailServerRecord;

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
public class XEmailServerDao extends AbstractVertxDAO<XEmailServerRecord, cn.vertxup.ambient.domain.tables.pojos.XEmailServer, String, Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>>, Future<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<XEmailServerRecord,cn.vertxup.ambient.domain.tables.pojos.XEmailServer,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     *      * @param vertx the vertx instance
     */
        public XEmailServerDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(XEmailServer.X_EMAIL_SERVER, cn.vertxup.ambient.domain.tables.pojos.XEmailServer.class, new JDBCClassicQueryExecutor<XEmailServerRecord,cn.vertxup.ambient.domain.tables.pojos.XEmailServer,String>(configuration,cn.vertxup.ambient.domain.tables.pojos.XEmailServer.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.ambient.domain.tables.pojos.XEmailServer object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByName(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.NAME.in(values));
        }

        /**
     * Find records that have <code>NAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByName(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.NAME.in(values),limit);
        }

        /**
     * Find records that have <code>IP_V4 IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByIpV4(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.IP_V4.in(values));
        }

        /**
     * Find records that have <code>IP_V4 IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByIpV4(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.IP_V4.in(values),limit);
        }

        /**
     * Find records that have <code>IP_V6 IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByIpV6(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.IP_V6.in(values));
        }

        /**
     * Find records that have <code>IP_V6 IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByIpV6(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.IP_V6.in(values),limit);
        }

        /**
     * Find records that have <code>HOSTNAME IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByHostname(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.HOSTNAME.in(values));
        }

        /**
     * Find records that have <code>HOSTNAME IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByHostname(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.HOSTNAME.in(values),limit);
        }

        /**
     * Find records that have <code>PORT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByPort(Collection<Integer> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PORT.in(values));
        }

        /**
     * Find records that have <code>PORT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByPort(Collection<Integer> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PORT.in(values),limit);
        }

        /**
     * Find records that have <code>PROTOCOL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByProtocol(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PROTOCOL.in(values));
        }

        /**
     * Find records that have <code>PROTOCOL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByProtocol(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PROTOCOL.in(values),limit);
        }

        /**
     * Find records that have <code>SENDER IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyBySender(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.SENDER.in(values));
        }

        /**
     * Find records that have <code>SENDER IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyBySender(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.SENDER.in(values),limit);
        }

        /**
     * Find records that have <code>PASSWORD IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByPassword(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PASSWORD.in(values));
        }

        /**
     * Find records that have <code>PASSWORD IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByPassword(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.PASSWORD.in(values),limit);
        }

        /**
     * Find records that have <code>OPTIONS IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByOptions(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.OPTIONS.in(values));
        }

        /**
     * Find records that have <code>OPTIONS IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByOptions(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.OPTIONS.in(values),limit);
        }

        /**
     * Find records that have <code>APP_ID IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByAppId(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.APP_ID.in(values));
        }

        /**
     * Find records that have <code>APP_ID IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByAppId(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.APP_ID.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.ambient.domain.tables.pojos.XEmailServer>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(XEmailServer.X_EMAIL_SERVER.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<XEmailServerRecord,cn.vertxup.ambient.domain.tables.pojos.XEmailServer,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<XEmailServerRecord,cn.vertxup.ambient.domain.tables.pojos.XEmailServer,String>) super.queryExecutor();
        }
}