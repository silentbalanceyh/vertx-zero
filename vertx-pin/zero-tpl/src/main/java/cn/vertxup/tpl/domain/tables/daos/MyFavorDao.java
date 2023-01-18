/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.tpl.domain.tables.daos;


import cn.vertxup.tpl.domain.tables.MyFavor;
import cn.vertxup.tpl.domain.tables.records.MyFavorRecord;

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
public class MyFavorDao extends AbstractVertxDAO<MyFavorRecord, cn.vertxup.tpl.domain.tables.pojos.MyFavor, String, Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>>, Future<cn.vertxup.tpl.domain.tables.pojos.MyFavor>, Future<Integer>, Future<String>> implements io.github.jklingsporn.vertx.jooq.classic.VertxDAO<MyFavorRecord,cn.vertxup.tpl.domain.tables.pojos.MyFavor,String> {

        /**
     * @param configuration The Configuration used for rendering and query
     * execution.
     * @param vertx the vertx instance
     */
        public MyFavorDao(Configuration configuration, io.vertx.core.Vertx vertx) {
                super(MyFavor.MY_FAVOR, cn.vertxup.tpl.domain.tables.pojos.MyFavor.class, new JDBCClassicQueryExecutor<MyFavorRecord,cn.vertxup.tpl.domain.tables.pojos.MyFavor,String>(configuration,cn.vertxup.tpl.domain.tables.pojos.MyFavor.class,vertx));
        }

        @Override
        protected String getId(cn.vertxup.tpl.domain.tables.pojos.MyFavor object) {
                return object.getKey();
        }

        /**
     * Find records that have <code>OWNER IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByOwner(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.OWNER.in(values));
        }

        /**
     * Find records that have <code>OWNER IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByOwner(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.OWNER.in(values),limit);
        }

        /**
     * Find records that have <code>OWNER_TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByOwnerType(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.OWNER_TYPE.in(values));
        }

        /**
     * Find records that have <code>OWNER_TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByOwnerType(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.OWNER_TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>UI_SORT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUiSort(Collection<Long> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.UI_SORT.in(values));
        }

        /**
     * Find records that have <code>UI_SORT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUiSort(Collection<Long> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.UI_SORT.in(values),limit);
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByType(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.TYPE.in(values));
        }

        /**
     * Find records that have <code>TYPE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByType(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.TYPE.in(values),limit);
        }

        /**
     * Find records that have <code>POSITION IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByPosition(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.POSITION.in(values));
        }

        /**
     * Find records that have <code>POSITION IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByPosition(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.POSITION.in(values),limit);
        }

        /**
     * Find records that have <code>URI_KEY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriKey(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_KEY.in(values));
        }

        /**
     * Find records that have <code>URI_KEY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriKey(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_KEY.in(values),limit);
        }

        /**
     * Find records that have <code>URI_FULL IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriFull(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_FULL.in(values));
        }

        /**
     * Find records that have <code>URI_FULL IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriFull(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_FULL.in(values),limit);
        }

        /**
     * Find records that have <code>URI IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUri(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI.in(values));
        }

        /**
     * Find records that have <code>URI IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUri(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI.in(values),limit);
        }

        /**
     * Find records that have <code>URI_PARAM IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriParam(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_PARAM.in(values));
        }

        /**
     * Find records that have <code>URI_PARAM IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUriParam(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.URI_PARAM.in(values),limit);
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByActive(Collection<Boolean> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.ACTIVE.in(values));
        }

        /**
     * Find records that have <code>ACTIVE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByActive(Collection<Boolean> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.ACTIVE.in(values),limit);
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyBySigma(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.SIGMA.in(values));
        }

        /**
     * Find records that have <code>SIGMA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyBySigma(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.SIGMA.in(values),limit);
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByMetadata(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.METADATA.in(values));
        }

        /**
     * Find records that have <code>METADATA IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByMetadata(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.METADATA.in(values),limit);
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByLanguage(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.LANGUAGE.in(values));
        }

        /**
     * Find records that have <code>LANGUAGE IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByLanguage(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.LANGUAGE.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByCreatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.CREATED_AT.in(values));
        }

        /**
     * Find records that have <code>CREATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByCreatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.CREATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByCreatedBy(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.CREATED_BY.in(values));
        }

        /**
     * Find records that have <code>CREATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByCreatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.CREATED_BY.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUpdatedAt(Collection<LocalDateTime> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.UPDATED_AT.in(values));
        }

        /**
     * Find records that have <code>UPDATED_AT IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUpdatedAt(Collection<LocalDateTime> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.UPDATED_AT.in(values),limit);
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUpdatedBy(Collection<String> values) {
                return findManyByCondition(MyFavor.MY_FAVOR.UPDATED_BY.in(values));
        }

        /**
     * Find records that have <code>UPDATED_BY IN (values)</code> asynchronously
     * limited by the given limit
     */
        public Future<List<cn.vertxup.tpl.domain.tables.pojos.MyFavor>> findManyByUpdatedBy(Collection<String> values, int limit) {
                return findManyByCondition(MyFavor.MY_FAVOR.UPDATED_BY.in(values),limit);
        }

        @Override
        public JDBCClassicQueryExecutor<MyFavorRecord,cn.vertxup.tpl.domain.tables.pojos.MyFavor,String> queryExecutor(){
                return (JDBCClassicQueryExecutor<MyFavorRecord,cn.vertxup.tpl.domain.tables.pojos.MyFavor,String>) super.queryExecutor();
        }
}
