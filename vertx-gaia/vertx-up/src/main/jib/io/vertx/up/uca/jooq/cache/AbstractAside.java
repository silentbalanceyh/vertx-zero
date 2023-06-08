package io.vertx.up.uca.jooq.cache;

import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Sorter;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.cache.hit.*;
import io.vertx.up.uca.jooq.ActionQr;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.uca.jooq.util.JqTool;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
abstract class AbstractAside {

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
        return this.messageUnique(field, value, point);
    }

    protected CMessage messageUniqueCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(point, 0);
        return this.messageUnique(condition, point);
    }

    protected CMessage messageUniquePojo(final ProceedingJoinPoint point) {
        return this.messageUnique(this.argumentPojo(point, 0), point);
    }

    /*
     * MessageList
     */
    protected CMessage messageListField(final ProceedingJoinPoint point) {
        final String field = this.argument(point, 0);
        final Object value = this.argument(point, 1);
        return this.messageList(field, value, point);
    }

    protected CMessage messageListCond(final ProceedingJoinPoint point) {
        final JsonObject condition = this.argument(point, 0);
        return this.messageList(condition, point);
    }

    protected CMessage messageListPojo(final ProceedingJoinPoint point) {
        return this.messageList(this.argumentPojo(point, 0), point);
    }

    /* CMessage -> CMessageKey -> <T> T method(Object) */
    protected CMessage messageKey(final Object id, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageKey(id, analyzer.type());
        message.bind(analyzer.primarySet());
        return message;
    }

    // ------------------ CMessage Collection -------------------------

    protected List<CMessage> messagesCond(final ProceedingJoinPoint point) {
        final Object idSet = this.argumentCond(point);
        return this.messages(idSet, point);
    }

    protected List<CMessage> messagesPojo(final ProceedingJoinPoint point, final int index) {
        final Object idSet = this.argumentCond(point, 0);
        return this.messages(idSet, point);
    }

    protected List<CMessage> messagesT(final ProceedingJoinPoint point) {
        final Object idSet = this.argumentT(point);
        return this.messages(idSet, point);
    }

    /* List<CMessage> -> List<CMessageTree> -> method(Object) */
    protected List<CMessage> messages(final Object args, final ProceedingJoinPoint point) {
        final List<CMessage> messageList = new ArrayList<>();
        if (Objects.nonNull(args)) {
            if (args instanceof Collection) {
                /*
                 * Collection of id set
                 */
                ((Collection) args).forEach(id -> messageList.add(this.messageTree(id, point)));
            } else {
                final Class<?> type = args.getClass();
                if (type.isArray()) {
                    /*
                     * Array of id set
                     */
                    Arrays.asList((Object[]) args).forEach(id -> messageList.add(this.messageTree(id, point)));
                } else {
                    /*
                     * Object ( reference )
                     */
                    messageList.add(this.messageTree(args, point));
                }
            }
        }
        return messageList;
    }
    // ------------------ Message Private -----------------------------

    /* CMessageUnique */
    /* CMessage -> CMessageUnique -> <T> T method(JsonObject) */
    private CMessage messageUnique(final JsonObject condition, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageUnique(condition, analyzer.type());
        message.bind(analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageUnique -> <T> T method(String, Object) */
    private CMessage messageUnique(final String field, final Object value, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageUnique(field, value, analyzer.type());
        message.bind(analyzer.primarySet());
        return message;
    }

    /* CMessageList */
    /* CMessage -> CMessageList -> <T> List<T> method(JsonObject) */
    private CMessage messageList(final JsonObject condition, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageList(condition, analyzer.type());
        message.bind(analyzer.primarySet());
        return message;
    }

    /* CMessage -> CMessageList -> <T> List<T> method(String, Object) */
    private CMessage messageList(final String field, final Object value, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageList(field, value, analyzer.type());
        message.bind(analyzer.primarySet());
        return message;
    }

    /* CMessageTree */
    private CMessage messageTree(final Object id, final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        final CMessage message = new CMessageTree(id, analyzer.type());
        message.bind(analyzer.primarySet());      // Bind data here
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
                final JqAnalyzer analyzer = this.analyzer(point);
                if (input instanceof Collection) {
                    /*
                     * Process Collection
                     */
                    final List<Object> idSet = new ArrayList<>();
                    ((Collection) input).stream().map(analyzer::primaryValue).forEach(idSet::add);
                    return idSet;
                } else {
                    if (input instanceof String) {
                        return input;
                    } else {
                        return analyzer.primaryValue(input);
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
                final JsonObject condition = ((JsonObject) input);

                /* Sorter Building  */
                final Sorter sorter;
                if (1 < args.length && args[1] instanceof Sorter) {
                    sorter = (Sorter) args[1];
                } else {
                    sorter = null;
                }
                return actionQr(point).searchPrimary(condition, sorter);
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
                /* Sorter Building  */
                final Sorter sorter;
                if (2 < args.length && args[2] instanceof Sorter) {
                    sorter = (Sorter) args[2];
                } else {
                    sorter = null;
                }
                return actionQr(point).searchPrimary(criteria, sorter);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected L1Aside executor(final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        return new L1Aside(analyzer);
    }

    private ActionQr actionQr(final ProceedingJoinPoint point) {
        final JqAnalyzer analyzer = this.analyzer(point);
        return new ActionQr(analyzer);
    }

    /*
     * Extract analyzer from target call
     * UxJooq object to avoid aspect duplicated
     */
    private JqAnalyzer analyzer(final ProceedingJoinPoint point) {
        return ((UxJooq) point.getTarget()).analyzer();
    }
}
