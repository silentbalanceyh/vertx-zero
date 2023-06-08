package io.vertx.up.commune.secure;

import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * Vis means `View`, the spelling is Denmark language instead of English
 * to avoid View word in future critical usage, also this word is simple
 * for using.
 *
 * The data structure is
 *
 * <pre><code class="json">
 *     {
 *        "position": "The position of current view",
 *        "view": "The view name of current"
 *     }
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Vis extends JsonObject {
    /*
     * Private Constructor
     * This data could be put into PointParam here
     */
    private Vis(final String view, final String position) {
        super();
        this.put(KName.VIEW, view);
        this.put(KName.POSITION, position);
    }

    private Vis(final JsonObject json) {
        super();
        if (Ut.isNotNil(json)) {
            this.mergeIn(json);
        }
    }

    public static Vis create(final JsonArray input) {
        final String view;
        final String position;
        if (Ut.isNil(input)) {
            /* Empty */
            view = io.horizon.eon.VValue.DFT.V_VIEW;
            position = io.horizon.eon.VValue.DFT.V_POSITION;
        } else {
            final String v = input.getString(VValue.IDX);
            view = Ut.isNil(v) ? io.horizon.eon.VValue.DFT.V_VIEW : v;
            if (1 < input.size()) {
                final String p = input.getString(VValue.ONE);
                position = Ut.isNil(p) ? io.horizon.eon.VValue.DFT.V_POSITION : p;
            } else {
                position = io.horizon.eon.VValue.DFT.V_POSITION;
            }
        }
        return new Vis(view, position);
    }

    /*
     * [view, position]
     * Here the sequence is reverted.
     */
    public static Vis create(final String literal) {
        if (Ut.isNil(literal)) {
            return create(new JsonArray());
        } else {
            /*
             * Normalized
             * If encoded the literal here, the literal should contains one of
             * [ - %5B
             * , - %2C
             * ] - %5D
             * This Fix should resolve the bug of `view` parameters
             */
            final String normalized;
            if (literal.contains("%5B") ||
                literal.contains("%2C") ||
                literal.contains("%5D")) {
                normalized = Ut.decryptUrl(literal);
            } else {
                normalized = literal;
            }
            final String detected = Ut.aiJArray(normalized);
            final JsonArray data = Ut.toJArray(detected);
            return create(data);
        }
    }

    public static Vis smart(final Object json) {
        if (json instanceof Vis) {
            // Vis object, convert directly
            return (Vis) json;
        } else if (json instanceof JsonObject) {
            // Json object convert to Vis ( sub class )
            return new Vis(((JsonObject) json));
        } else if (json instanceof final String viewJson) {
            if (Ut.isJObject(viewJson)) {
                // The json is literal
                return new Vis(Ut.toJObject(viewJson));
            } else if (Ut.isJArray(viewJson)) {
                // String literal
                return create(viewJson);
            } else {
                // Single view with default position
                return new Vis((String) json, io.horizon.eon.VValue.DFT.V_POSITION);
            }
        } else if (json instanceof final JsonArray jsonArray) {
            // JsonArray
            return create(jsonArray);
        } else {
            // Default value
            return new Vis(io.horizon.eon.VValue.DFT.V_VIEW, io.horizon.eon.VValue.DFT.V_POSITION);
        }
    }

    public String view() {
        return this.getString(KName.VIEW, io.horizon.eon.VValue.DFT.V_VIEW);
    }

    public String position() {
        return this.getString(KName.POSITION, io.horizon.eon.VValue.DFT.V_POSITION);
    }
}
