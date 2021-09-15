package io.vertx.tp.crud.uca.desk;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.em.ApiSpec;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.tp.ke.cv.em.JoinMode;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * Here are the new data structure for input data
 * 1) Envelop will convert to IxOpt to panel
 * 2) Here are different module extractor in internal code logical for connect
 * 3) This component will provide the feature that are similar with IxNext ( Old Version )
 * 4) Provide the mapping from `active` to `standBy`
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IxWeb {
    private final static String LOGGER_MOD = "active=\u001b[1;36m{0}\u001b[m, standby=\u001b[1;95m{1}\u001b[m, api={2}, view={3}";
    private final transient ApiSpec apiSpecification;
    // IxMod Calculation
    private transient IxMod active;
    private transient IxMod standBy;
    // Input Parameters
    private transient Vis view;
    private transient String key;
    private transient JsonObject bodyJ;
    private transient JsonArray bodyA;

    private IxWeb(final ApiSpec spec) {
        this.apiSpecification = spec;
    }

    public static IxWeb create(final ApiSpec spec) {
        return new IxWeb(spec);
    }

    public IxWeb build(final Envelop envelop) {
        /*
         *  All the apis support the first parameter of actor
         *  Here we create the first module ( active module )
         */
        final String actor = Ux.getString(envelop);
        this.active = IxMod.create(actor).bind(envelop);

        // Different workflow to extract rest parameters
        String module = null;
        if (ApiSpec.BODY_JSON == this.apiSpecification) {
            // actor       body                key         module          view
            // 0           1 ( JObject )
            // 0           1 ( JCriteria )                  2              3
            this.bodyJ = Ux.getJson1(envelop);
            module = Ux.getString2(envelop);
            this.view = Ux.getVis(envelop, 3);
        } else if (ApiSpec.BODY_STRING == this.apiSpecification) {
            // actor       body                key         module          view
            // 0           1 ( filename )                   2
            // 0                               1
            this.key = Ux.getString1(envelop);
        } else if (ApiSpec.BODY_ARRAY == this.apiSpecification) {
            // actor       body                key         module          view
            // 0           1 ( JArray )
            // 0           1 ( JArray )                     2
            this.bodyA = Ux.getArray1(envelop);
            module = Ux.getString2(envelop);
        } else if (ApiSpec.BODY_WITH_KEY == this.apiSpecification) {
            // actor       body                key         module          view
            // 0           2                   1
            this.key = Ux.getString1(envelop);
            this.bodyJ = Ux.getJson2(envelop);
        } else if (ApiSpec.BODY_NONE == this.apiSpecification) {
            // actor       body                key         module          view
            // 0                                            1              2
            module = Ux.getString1(envelop);
            this.view = Ux.getVis2(envelop);
        }

        // Apply the default view information
        if (Objects.isNull(this.view)) {
            this.view = Vis.smart(null);
        }

        // Apply the default view information
        {
            /*
             * 1. When ADD / UPDATE
             *    1.1. P1: `module` parameter is the first priority
             *    1.2. P2: When `module` has not been provided, here should be SMART processing on BODY
             *    1.3. P3: The default workflow
             *
             * 2. Other situations
             *    2.1. P1: `module` parameter is the first priority
             *    2.2. P2: The default workflow
             */
            final KModule active = this.active.module();
            final KJoin connect = active.getConnect();
            /*
             * When `KJoin` is null, it means that current module does not support any
             * extension for sub-modules, in this situation, clear the module parameters
             * because it's useless.
             */
            if (Objects.nonNull(connect)) {
                final KPoint target;
                if (Objects.isNull(module)) {
                    /*
                     * Here are no `module` parameters
                     */
                    if (Objects.nonNull(this.bodyJ)) {
                        target = connect.procTarget(this.bodyJ);
                    } else if (Objects.nonNull(this.bodyA)) {
                        target = connect.procTarget(this.bodyA);
                    } else {
                        target = null;
                    }
                } else {
                    /*
                     * Here are `module` parameters
                     */
                    target = connect.procTarget(module);
                }
                /*
                 * This condition means that you can build standBy then because ths standBy found
                 */
                if (Objects.nonNull(target) && JoinMode.CRUD == target.modeTarget()) {
                    this.standBy = IxMod.create(target.getCrud()).bind(envelop);
                    this.active.connect(this.standBy);
                }
            }
        }
        Ix.Log.web(this.getClass(), this.LOGGER_MOD,
            this.active.module().getIdentifier(),
            Objects.nonNull(this.standBy) ? this.standBy.module().getIdentifier() : null,
            this.apiSpecification, this.view.view() + ":" + this.view.position());
        return this;
    }

    public IxMod active() {
        return this.active;
    }

    public IxMod standBy() {
        return this.standBy;
    }

    public JsonObject dataK() {
        return new JsonObject().put(KName.KEY, this.key);
    }

    public JsonObject dataF() {
        return new JsonObject().put(KName.FILE_NAME, this.key);
    }

    public JsonObject dataJ() {
        return this.bodyJ;
    }

    public JsonObject dataV() {
        return new JsonObject().put(KName.VIEW, this.view);
    }

    public JsonArray dataA() {
        return this.bodyA;
    }
}
