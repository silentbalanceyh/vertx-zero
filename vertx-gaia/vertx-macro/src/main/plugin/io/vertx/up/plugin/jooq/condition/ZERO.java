package io.vertx.up.plugin.jooq.condition;

import io.horizon.uca.qr.syntax.Ir;
import io.vertx.up.plugin.jooq.condition.date.*;
import io.vertx.up.util.Ut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Info {

    String JOOQ_PARSE = "( Jooq -> Condition ) Parsed result is condition = {0}.";
    String JOOQ_CACHED = "( Jooq -> Condition ) Cached result is condition = {0}.";
    String JOOQ_TERM = "`io.vertx.mod.plugin.jooq.condition.Term` selected: `{0}` by op = `{1}`.";
    String JOOQ_TERM_ERR = "`io.vertx.mod.plugin.jooq.condition.Term` is null when op = `{0}`.";
}

interface Pool {

    ConcurrentMap<Class<?>, Clause> CLAUSE_MAP = new ConcurrentHashMap<Class<?>, Clause>() {
        {
            this.put(Object.class, Ut.instance(ClauseString.class));
            this.put(Boolean.class, Ut.instance(ClauseBoolean.class));
            this.put(LocalDateTime.class, Ut.instance(ClauseInstant.class));
            this.put(LocalDate.class, Ut.instance(ClauseInstant.class));
            this.put(LocalTime.class, Ut.instance(ClauseInstant.class));
            this.put(Number.class, Ut.instance(ClauseNumber.class));
            this.put(Long.class, Ut.instance(ClauseNumber.class));
            this.put(Integer.class, Ut.instance(ClauseNumber.class));
            this.put(Short.class, Ut.instance(ClauseNumber.class));
        }
    };

    ConcurrentMap<String, Term> TERM_OBJECT_MAP = new ConcurrentHashMap<String, Term>() {
        {
            this.put(Ir.Op.LT, Ut.instance(TermLt.class));
            this.put(Ir.Op.LE, Ut.instance(TermLe.class));
            this.put(Ir.Op.GT, Ut.instance(TermGt.class));
            this.put(Ir.Op.GE, Ut.instance(TermGe.class));
            this.put(Ir.Op.EQ, Ut.instance(TermEq.class));
            this.put(Ir.Op.NEQ, Ut.instance(TermNeq.class));
            this.put(Ir.Op.NULL, Ut.instance(TermNull.class));
            this.put(Ir.Op.NOT_NULL, Ut.instance(TermNotNull.class));
            this.put(Ir.Op.TRUE, Ut.instance(TermTrue.class));
            this.put(Ir.Op.FALSE, Ut.instance(TermFalse.class));
            this.put(Ir.Op.IN, Ut.instance(TermIn.class));
            this.put(Ir.Op.NOT_IN, Ut.instance(TermNotIn.class));
            this.put(Ir.Op.START, Ut.instance(TermStart.class));
            this.put(Ir.Op.END, Ut.instance(TermEnd.class));
            this.put(Ir.Op.CONTAIN, Ut.instance(TermContain.class));
        }
    };

    ConcurrentMap<String, Term> TERM_DATE_MAP = new ConcurrentHashMap<String, Term>() {
        {
            this.put(Ir.Op.LT, Ut.instance(TermDLt.class));
            this.put(Ir.Op.LE, Ut.instance(TermDLe.class));
            this.put(Ir.Op.GT, Ut.instance(TermDGt.class));
            this.put(Ir.Op.GE, Ut.instance(TermDGe.class));
            this.put(Ir.Op.EQ, Ut.instance(TermDEq.class));
        }
    };
}