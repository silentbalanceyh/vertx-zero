package io.vertx.up.uca.wffs;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.wffs.script.Inlet;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.*;

import java.io.Serializable;

/**
 * The design for playbook here
 *
 * 1) The default variable for usage
 * - zo: Zero Old, the previous record of operation
 * - zn: Zero New, the current record of operation
 * - zw: Zero Workflow, the workflow definition here
 * - zt: Zero Toolkit, this toolkit could contain some functions and you can call `zt.xxx` to do execution, it will let expression more smart and dynamic
 *
 * 2) The rule of playbook
 * - Check the condition for pre/post expression
 * - Eval the message/string for usage to output based on template
 *
 * 3) Here are some specific data structure in the first version:
 * This structure is inner `zo/zn` instead of other situations and it's for workflow engine
 *
 * The root data is ( Ticket / Extension / Todo )
 *
 * // <pre><code class="json">
 *     {
 *         "user": {}       // The user map:  key = name ( real name )
 *         "record": {}     // Related Record ( Json / Array )
 *         "linkage": {}    // All Linkage Data, each data is Json Array
 *     }
 * // </code></pre>
 *
 * The normalized variable should be:
 *
 * - zn.user:       user
 * - zn.record:     record
 * - zn.linkage:    linkage
 *
 * If you use `zo`, it means the previous stored information.
 *
 * Above data structure is for workflow engine only
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Playbook implements Serializable {
    private static final Annal LOGGER = Annal.get(Playbook.class);
    private static final JexlEngine EXPR = new JexlBuilder().cache(4096).silent(false).create();

    private final String expression;
    private final transient JsonObject tpl = new JsonObject();

    private Playbook(final String expression) {
        this.expression = expression;
    }

    public static Playbook open(final String expression) {
        return new Playbook(expression);
    }

    public Playbook bind(final JsonObject tpl) {
        final JsonObject tplJ = Ut.valueJObject(tpl);
        this.tpl.mergeIn(tplJ, true);
        return this;
    }

    public Future<Boolean> isOk(final JsonObject params) {
        final JexlScript script = EXPR.createScript(this.expression);
        final JexlContext context = this.buildContext(params, true);

        /* Script Execute on expression */
        final Boolean checked = (Boolean) script.execute(context);
        LOGGER.info("[ Script ] isOk = {2}, The expression = `{0}` has been parsed to `{1}`.",
            this.expression, script.getParsedText(), checked);
        return Future.succeededFuture(checked);
    }

    public Future<String> format(final JsonObject params) {
        final String formattedExpr = "`" + this.expression + "`";
        final JexlExpression expression = EXPR.createExpression(formattedExpr);
        final JexlContext context = this.buildContext(params, false);
        final String formatted = (String) expression.evaluate(context);
        LOGGER.info("[ Script ] Message = {2}, The expression = `{0}` has been parsed to `{1}`.",
            this.expression, expression.getParsedText(), formatted);
        return Future.succeededFuture(formatted);
    }

    private JexlContext buildContext(final JsonObject params, final boolean prefix) {
        final JexlContext context = new MapContext();
        /*
         * $zo = OLD Data
         * $zn = NEW Data
         */
        final Inlet zData = Inlet.data(prefix);
        zData.compile(context, params, this.tpl);

        /*
         * $zw = Workflow Data
         */
        final Inlet zFlow = Inlet.flow(prefix);
        zFlow.compile(context, params, this.tpl);

        /*
         * $uo = Old User
         * $un = New User
         * $lo = `updatedBy` field value ( Current User )
         */
        final Inlet zUser = Inlet.user(prefix);
        zUser.compile(context, params, this.tpl);
        return context;
    }
}
