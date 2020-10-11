package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.*;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.ActionQr;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.util.JqTool;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractAside {
    /*
     * L1 Aside executing for cache
     */
    protected transient L1Aside executor;
    protected transient JqAnalyzer analyzer;
    private transient ActionQr actionQr;


    protected void initialize(final Class<?> clazz, final VertxDAO vertxDAO) {
        final JqAnalyzer analyzer = JqAnalyzer.create(vertxDAO);
        this.analyzer = analyzer;
        this.executor = new L1Aside(analyzer);
        this.actionQr = new ActionQr(this.analyzer);
    }

    /*
     * Logger that will be used in L1 cache sub-classes
     */
    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /*
     * CMessage object creation here, there are two format
     * 1) The parameter is `ProceedingJoinPoint` only
     * 2) The following method is for different signature
     */
    // ------------------ Directly Calling -------------------------
    /*
     * - <T> T messageField(ProceedingJoinPoint)
     *         |--> (String, Object)
     * - <T> List<T> messagesField(ProceedingJoinPoint)
     *         |--> (String, Object)
     *
     * - <T> T messageCond(ProceedingJoinPoint)
     *         |--> (JsonObject)
     * - <T> List<T> messagesCond(ProceedingJoinPoint)
     *         |--> (JsonObject)
     */
    /*
     * MessageUnique
     */
    protected CMessage messageUniqueField(final ProceedingJoinPoint point) {
        final String field = this.argument(point, 0);
        final Object value = this.argument(point, 1);
        return this.messageUnique(field, value);
    }

    protected CMessage messageUniqueCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(point, 0);
        return this.messageUnique(condition);
    }

    protected CMessage messageUniquePojo(final ProceedingJoinPoint point) {
        return this.messageUnique(this.argumentPojo(point, 0));
    }

    /*
     * MessageList
     */
    protected CMessage messageListField(final ProceedingJoinPoint point) {
        final String field = this.argument(point, 0);
        final Object value = this.argument(point, 1);
        return this.messageList(field, value);
    }

    protected CMessage messageListCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(point, 0);
        return this.messageList(condition);
    }

    protected CMessage messageListPojo(final ProceedingJoinPoint point) {
        return this.messageList(this.argumentPojo(point, 0));
    }

    /* CMessage -> CMessageKey -> <T> T method(Object) */
    protected CMessage messageKey(final Object id) {
        final CMessage message = new CMessageKey(id, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    // ------------------ CMessage Collection -------------------------

    protected List<CMessage> messagesCond(final ProceedingJoinPoint point) {
        final Object idSet = this.argumentCond(point);
        return this.messages(idSet);
    }

    protected List<CMessage> messagesPojo(final ProceedingJoinPoint point, final int index) {
        final Object idSet = this.argumentCond(point, 0);
        return this.messages(idSet);
    }

    protected List<CMessage> messagesT(final ProceedingJoinPoint point) {
        final Object idSet = this.argumentT(point);
        return this.messages(idSet);
    }

    /* List<CMessage> -> List<CMessageTree> -> method(Object) */
    protected List<CMessage> messages(final Object args) {
        final List<CMessage> messageList = new ArrayList<>();
        if (Objects.nonNull(args)) {
            if (args instanceof Collection) {
                /*
                 * Collection of id set
                 */
                ((Collection) args).forEach(id -> messageList.add(this.messageTree(id)));
            } else {
                final Class<?> type = args.getClass();
                if (type.isArray()) {
                    /*
                     * Array of id set
                     */
                    Arrays.asList((Object[]) args).forEach(id -> messageList.add(this.messageTree(id)));
                } else {
                    /*
                     * Object ( reference )
                     */
                    messageList.add(this.messageTree(args));
                }
            }
        }
        return messageList;
    }
    // ------------------ Message Private -----------------------------

    /* CMessageUnique */
    /* CMessage -> CMessageUnique -> <T> T method(JsonObject) */
    private CMessage messageUnique(final JsonObject condition) {
        final CMessage message = new CMessageUnique(condition, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageUnique -> <T> T method(String, Object) */
    private CMessage messageUnique(final String field, final Object value) {
        final CMessage message = new CMessageUnique(field, value, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessageList */
    /* CMessage -> CMessageList -> <T> List<T> method(JsonObject) */
    private CMessage messageList(final JsonObject condition) {
        final CMessage message = new CMessageList(condition, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageList -> <T> List<T> method(String, Object) */
    private CMessage messageList(final String field, final Object value) {
        final CMessage message = new CMessageList(field, value, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessageTree */
    private CMessage messageTree(final Object id) {
        final CMessage message = new CMessageTree(id, this.analyzer.type());
        message.bind(this.analyzer.primarySet());      // Bind data here
        return message;
    }
    // ------------------ Argument Private ----------------------------

    /*
     * Condition + Pojo
     */
    private JsonObject argumentPojo(final ProceedingJoinPoint point, final int start) {
        final JsonObject condition = this.argument(point, start);
        final Object[] args = point.getArgs();
        final String pojo = this.argument(point, args.length - 1);
        return JqTool.criteria(condition, pojo);
    }

    /*
     * Argument extraction here based on `index`
     *
     * For example:
     * - method(arg1,arg2,arg3,....)
     *
     * The parameters are:
     * - arg1 ( index = 0 )
     * - arg2 ( index = 1 )
     * - arg3 ( index = 2 )
     * ......
     * - argN ( index = N - 1 )
     */
    private <T> T argument(final ProceedingJoinPoint point, final Integer index) {
        final Object[] args = point.getArgs();
        if (index < args.length) {
            return (T) args[index];
        } else {
            return null;
        }
    }

    /*
     * Process two mode arguments in method definition such as:
     *
     * <T> T method(T)
     * <T> List<T> method(List<T>)
     *
     * Here this method process:
     * List<T> / T ----> List<Object> / Object ( ID Set )
     */
    private Object argumentT(final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        if (1 == args.length) {
            final Object input = args[0];
            if (Objects.isNull(input)) {
                return null;
            } else {
                if (input instanceof Collection) {
                    /*
                     * Process Collection
                     */
                    final List<Object> idSet = new ArrayList<>();
                    ((Collection) input).stream().map(this.analyzer::primaryValue).forEach(idSet::add);
                    return idSet;
                } else {
                    if (input instanceof String) {
                        return input;
                    } else {
                        return this.analyzer.primaryValue(input);
                    }
                }
            }
        } else {
            return null;
        }
    }

    /*
     * index = 0, JsonObject
     * index = x, JsonObject, pojo = length - 1
     */
    private Object argumentCond(final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        if (0 < args.length) {
            final Object input = args[0];
            if (input instanceof JsonObject) {
                /*
                 * Get primaryValues
                 */
                return actionQr.searchPrimary((JsonObject) input);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Object argumentCond(final ProceedingJoinPoint point, final int index) {
        final Object[] args = point.getArgs();
        if (index < args.length) {
            final Object input = args[index];
            if (input instanceof JsonObject) {
                final JsonObject condition = (JsonObject) input;
                final String pojo = (String) args[args.length - 1];
                final JsonObject criteria = JqTool.criteria(condition, pojo);
                return actionQr.searchPrimary(criteria);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
