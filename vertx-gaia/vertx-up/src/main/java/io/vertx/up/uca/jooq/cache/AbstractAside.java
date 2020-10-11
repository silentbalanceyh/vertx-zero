package io.vertx.up.uca.jooq.cache;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.hit.*;
import io.vertx.up.log.Annal;
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

    protected void initialize(final Class<?> clazz, final VertxDAO vertxDAO) {
        final JqAnalyzer analyzer = JqAnalyzer.create(vertxDAO);
        this.analyzer = analyzer;
        this.executor = new L1Aside(analyzer);
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
     */
    protected CMessage messageField(final ProceedingJoinPoint point) {
        final String field = this.argument(0, point);
        final Object value = this.argument(1, point);
        return this.message(field, value);
    }

    protected CMessage messagesField(final ProceedingJoinPoint point) {
        final String field = this.argument(0, point);
        final Object value = this.argument(1, point);
        return this.messageList(field, value);
    }

    /*
     * - <T> T messageCond(ProceedingJoinPoint)
     *         |--> (JsonObject)
     * - <T> List<T> messagesCond(ProceedingJoinPoint)
     *         |--> (JsonObject)
     */
    protected CMessage messageCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(0, point);
        return this.message(condition);
    }

    protected CMessage messagesCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(0, point);
        return this.messages(condition);
    }

    // ------------------ CMessage Creation ( Element Only ) -------------------------
    /* CMessage -> CMessageKey -> <T> T method(Object) */
    protected CMessage message(final Object id) {
        final CMessage message = new CMessageKey(id, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageUnique -> <T> T method(JsonObject) */
    protected CMessage message(final JsonObject condition) {
        final CMessage message = new CMessageUnique(condition, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageUnique -> <T> T method(String, Object) */
    protected CMessage message(final String field, final Object value) {
        final CMessage message = new CMessageUnique(field, value, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageList -> <T> List<T> method(String, Object) */
    protected CMessage messageList(final String field, final Object value) {
        final CMessage message = new CMessageList(field, value, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageList -> <T> List<T> method(JsonObject) */
    protected CMessage messages(final JsonObject condition) {
        final CMessage message = new CMessageList(condition, this.analyzer.type());
        message.bind(this.analyzer.primarySet());
        return message;
    }

    /* List<CMessage> -> List<CMessageTree> -> method(Object) */
    protected List<CMessage> messageList(final Object args) {
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

    private CMessage messageTree(final Object id) {
        final CMessage message = new CMessageTree(id, this.analyzer.type());
        message.bind(this.analyzer.primarySet());      // Bind data here
        return message;
    }

    // ------------------ Pojo Processing -------------------------


    protected CMessage messagePojo(final ProceedingJoinPoint point) {
        return this.message(this.argumentPojo(0, point));
    }

    protected CMessage messagesPojo(final ProceedingJoinPoint point) {
        return this.messages(this.argumentPojo(0, point));
    }
    // ------------------ Argument processing -------------------------

    /*
     * Condition + Pojo
     */
    private JsonObject argumentPojo(final int start, final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(start, point);
        final Object[] args = point.getArgs();
        final String pojo = this.argument(args.length - 1, point);
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
    private <T> T argument(final Integer index, final ProceedingJoinPoint point) {
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
    protected Object argumentT(final ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        if (1 == args.length) {
            final Object input = args[0];
            return this.argumentTValue(input);
        } else {
            return null;
        }
    }

    private Object argumentTValue(final Object input) {
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
    }
}
