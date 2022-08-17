package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * 方便编程的简易结构，可对接后端专用的底层语法，由于 Qr 启用了 AOP 的缓存，所以不可以再追加API来执行相关操作，
 * 为了保证后期设计过程中 Redis 的缓存生效，使用新的语法来执行简易编程操作，并内置 ORDER BY 的常用语法
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QSyntax {
    /*
     * {
     *     ....,
     *     "__sort": [
     *         "xxx"
     *     ]
     * }
     */
    private final JsonObject syntax;
    private final Sorter sorter = Sorter.create();

    private QSyntax(final Qr.Connector connector) {
        if (Qr.Connector.OR == connector) {
            this.syntax = Ux.whereOr();
        } else {
            this.syntax = Ux.whereAnd();
        }
    }

    public static QSyntax and() {
        return new QSyntax(Qr.Connector.AND);
    }

    public static QSyntax or() {
        return new QSyntax(Qr.Connector.OR);
    }

    // ------------------- Criteria API to build WHERE ------------------
    public QSyntax put(final String field, final Object value) {
        this.syntax.put(field, value);
        return this;
    }

    public QSyntax eq(final String field, final Object value) {
        return this.put(this.field(field) + Strings.COMMA + Qr.Op.EQ, value);
    }

    public QSyntax neq(final String field, final Object value) {
        this.syntax.put(this.field(field) + Strings.COMMA + Qr.Op.NEQ, value);
        return this;
    }

    public QSyntax in(final String field, final Object value) {
        this.syntax.put(this.field(field) + Strings.COMMA + Qr.Op.IN, value);
        return this;
    }

    public QSyntax inNot(final String field, final Object value) {
        this.syntax.put(this.field(field) + Strings.COMMA + Qr.Op.NOT_IN, value);
        return this;
    }

    public QSyntax nil(final String field) {
        this.syntax.put(this.field(field) + Strings.COMMA + Qr.Op.NULL, Strings.EMPTY);
        return this;
    }

    public QSyntax nilNot(final String field) {
        this.syntax.put(this.field(field) + Strings.COMMA + Qr.Op.NOT_NULL, Strings.EMPTY);
        return this;
    }

    // ------------------- Sorter ------------------
    public QSyntax sort() {
        this.sorter.clear();
        return this;
    }

    public QSyntax asc(final String field) {
        this.sorter.add(field, true);
        return this;
    }

    public QSyntax desc(final String field) {
        this.sorter.add(field, false);
        return this;
    }

    private String field(final String field) {
        Objects.requireNonNull(field);
        if (field.contains(Strings.COMMA)) {
            return field.split(Strings.COMMA)[0];
        } else {
            return field;
        }
    }

    /*
     * 特殊语法
     * {
     *     "....",
     *     "__sort": {
     *     }
     * }
     */
    public JsonObject to() {
        final JsonObject criteria = this.syntax.copy();
        if (this.sorter.valid()) {
            criteria.put(KName.__.SORT, this.sorter.toJson());
        }
        return criteria;
    }
}
