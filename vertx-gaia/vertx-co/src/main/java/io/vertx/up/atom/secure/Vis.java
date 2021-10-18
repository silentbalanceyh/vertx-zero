package io.vertx.up.atom.secure;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Values;
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
        if (Ut.notNil(json)) {
            this.mergeIn(json);
        }
    }

    public static Vis create(final JsonArray input) {
        final String view;
        final String position;
        if (Ut.isNil(input)) {
            /* Empty */
            view = KValue.View.VIEW_DEFAULT;
            position = KValue.View.POSITION_DEFAULT;
        } else {
            final String v = input.getString(Values.IDX);
            view = Ut.isNil(v) ? KValue.View.VIEW_DEFAULT : v;
            if (1 < input.size()) {
                final String p = input.getString(Values.ONE);
                position = Ut.isNil(p) ? KValue.View.POSITION_DEFAULT : p;
            } else {
                position = KValue.View.POSITION_DEFAULT;
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
             */
            final String normalized = Ut.aiStringA(literal);
            final JsonArray data = Ut.toJArray(normalized);
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
        } else if (json instanceof String) {
            final String viewJson = (String) json;
            if (Ut.isJObject(viewJson)) {
                // The json is literal
                return new Vis(Ut.toJObject(viewJson));
            } else {
                // Single view with default position
                return new Vis((String) json, KValue.View.POSITION_DEFAULT);
            }
        } else {
            // Default value
            return new Vis(KValue.View.VIEW_DEFAULT, KValue.View.POSITION_DEFAULT);
        }
    }

    public String view() {
        return this.getString(KName.VIEW, KValue.View.VIEW_DEFAULT);
    }

    public String position() {
        return this.getString(KName.POSITION, KValue.View.POSITION_DEFAULT);
    }
}
