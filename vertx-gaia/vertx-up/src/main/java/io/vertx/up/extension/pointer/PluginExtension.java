package io.vertx.up.extension.pointer;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.unity.Ux;

import java.util.Set;

/*
 * Plugin Instance Pool for different usage.
 */
public interface PluginExtension {
    // ------------------ Internal Interface -----------------------

    interface Loader {
        /*
         * For Dynamic Modeling Extract
         */
        static HAtom atom(final String namespace, final String identifier) {
            /* Dynamic Atom Plug-In */
            return PluginLoad.atom(namespace, identifier);
        }
    }

    // ------------------ Sync Mode Plugin -----------------------
    interface ZeroRegistry {
        /*
         * io.vertx.up.uca.micro.center
         *      ZeroRegistry.registryRoute
         *
         * Registry the routes in etcd mode to replace :actor parameters such as
         * POST /api/:actor -> POST /api/group ( instead )
         * It's for zero extension module CRUD only, because CRUD module support
         * generic parameters {actor} as module identifier
         *
         * This parameter could connect business module cross
         * 1) Static business: Zero Standard
         * 2) Ox business: Data Driven Workflow
         */
        static void registryRoute(final Set<String> routes) {
            /* Etcd Registry Plugin */
            PluginRegistry.registry(routes);
        }
    }

    // ------------------ Async Mode Plugin ----------------------
    interface Answer {
        /*
         * io.vertx.up.uca.rs.hunt
         *      Answer.reply
         *
         */
        static Future<Envelop> reply(final RoutingContext context, final Envelop envelop) {
            /*
             * DataRegion for processing response.
             * 1）Get bound data from cache ( Async cache here );
             * 2) `projection` will impact ( ArrayList / Single Record );
             * 3) `rows` should impact collection only
             *    -- Limitation: could not impact PageList interface.
             */
            final HttpStatusCode code = envelop.status();

            return (HttpStatusCode.OK == code) ?
                /*
                 * Ok response enabled plugin
                 */
                PluginRegion.after(context, envelop) :
                /*
                 * Otherwise failure throw out directly
                 */
                Ux.future(envelop);
        }
    }

    interface Flower {
        /*
         * The same path for class/method definition.
         * io.vertx.up.uca.rs.hunt
         *      Flower.next
         */
        static Future<Envelop> next(final RoutingContext context, final Envelop envelop) {
            /*
             * Auditor injection
             * The four fields that will be injected into Envelop
             * 1) Create
             *    createdAt, createdBy
             * 2) Update
             *    updatedAt, updatedBy
             */
            return PluginAuditor.audit(context, envelop)
                /*
                 * DataRegion before
                 * Parameter will be modified
                 * 1) Could support POST/PUT only
                 * 2) The standard query engine parameters is as:
                 * {
                 *     criteria: {},
                 *     sorter: [],
                 *     projection: [],
                 *     pager:{
                 *         page: xx,
                 *         size: xx
                 *     }
                 * }
                 * 3) The `criteria` / `projection` will be modified
                 * 4) Only impact on ArrayList / Collection
                 */
                .compose(processed -> PluginRegion.before(context, processed));
            /* Auditor */
            // PluginAuditor.audit(context, envelop);

            /* DataRegion before */
            // PluginRegion.before(context, envelop);
        }
    }
}