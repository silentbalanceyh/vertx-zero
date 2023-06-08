package io.vertx.up.extension.dot;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.spi.cloud.query.HQBE;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;

/*
 * Infusion Instance Pool for different usage.
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

    // ------------------ Sync Mode Infusion -----------------------
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
            /* Etcd Registry Infusion */
            PluginRegistry.registry(routes);
        }
    }

    // ------------------ Async Mode Infusion ----------------------
    interface Answer {
        /*
         * io.vertx.up.container.hunt
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
                 * Otherwise handler throw out directly
                 */
                Ux.future(envelop);
        }
    }

    interface Flower {
        /*
         * The same path for class/method definition.
         * io.vertx.up.container.hunt
         *      Flower.next
         */
        static Future<Envelop> next(final RoutingContext context, final Envelop input) {
            /*
             * Add new workflow for HQBE to parsing the QBE workflow
             */
            return Future.succeededFuture(input)
                /*
                 * QBE for new workflow based on
                 * xxxx ? QBE = xxxxxxxx
                 */
                .compose(envelop -> nextQBE(context, envelop))
                /*
                 * Auditor injection
                 * The four fields that will be injected into Envelop
                 * 1) Create
                 *    createdAt, createdBy
                 * 2) Update
                 *    updatedAt, updatedBy
                 */
                .compose(envelop -> PluginAuditor.audit(context, envelop))
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
        }

        private static Future<Envelop> nextQBE(final RoutingContext context, final Envelop envelop) {
            /*
             * HQBE 新流程，流程执行结果如
             * HQBE -> ServiceLoader -> HQBEView
             *                            -> HQR 或标准流程
             */
            final String qbe = context.request().getParam(KName.QBE);
            final HttpMethod method = context.request().method();
            if (Ut.isNil(qbe) || HttpMethod.POST != method) {
                /*
                 * GET / PUT 跳过
                 */
                return Future.succeededFuture(envelop);
            }
            return Ux.channel(HQBE.class, () -> envelop, hqbe -> {
                /*
                 * 1. 先做Base64的解码
                 * 2. 再根据解码结果隐式替换 Envelop 中的 criteria 部分，QR 专用
                 */
                final JsonObject qbeJ = Ut.toJObject(Ut.decryptBase64(qbe));
                return hqbe.before(qbeJ, envelop);
            });
        }
    }
}