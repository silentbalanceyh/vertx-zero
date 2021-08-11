package io.vertx.tp.jet.refine;

import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.up.eon.KName;
import io.vertx.tp.optic.environment.Ambient;
import io.vertx.up.commune.exchange.DualMapping;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.exchange.DictConfig;
import io.vertx.up.eon.em.MappingMode;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/*
 * All dict / identity / dualMapping
 * have been put into pool structure
 */
class JtBusiness {
    static DictConfig toDict(final IService service) {
        return Fn.getNull(null, () -> Fn.pool(Pool.POOL_DICT, service.getKey(), () -> {
            /*
             * Dict Config for service
             */
            final String dictStr = service.getDictConfig();
            final DictConfig dict = new DictConfig(dictStr);
            /*
             * When valid, inject component here
             */
            if (!dict.getSource().isEmpty()) {
                final Class<?> component =
                        Ut.clazz(service.getDictComponent(), null);
                dict.bind(component);
                /*
                 * dictEpsilon configuration
                 */
                final JsonObject epsilonJson = Ut.toJObject(service.getDictEpsilon());
                dict.bind(Ux.dictEpsilon(epsilonJson));
            }
            /*
             * If Dict is not required, means
             * 1) The component could not be found
             * 2) The Dict Source configured list is empty, it's not needed
             */
            return dict;
        }), service);
    }

    static DualMapping toMapping(final IService service) {
        return Fn.getNull(null, () -> Fn.pool(Pool.POOL_MAPPING, service.getKey(), () -> {
            /*
             * DualMapping
             */
            final MappingMode mode = Ut.toEnum(service::getMappingMode, MappingMode.class, MappingMode.NONE);
            final DualMapping mapping = new DualMapping();
            /*
             * The mode != NONE means that there must contain configuration
             */
            final JsonObject config = Ut.toJObject(service.getMappingConfig());
            /*
             * 「Optional」The component inject
             */
            final Class<?> component = Ut.clazz(service.getMappingComponent(), null);
            mapping.init(config).bind(mode).bind(component);
            return mapping;
        }), service);
    }

    static Identity toIdentify(final IService service) {
        return Fn.getNull(null, () -> Fn.pool(Pool.POOL_IDENTITY, service.getKey(), () -> {
            /*
             * Identity for `identifier` processing
             */
            final Identity identity = new Identity();
            identity.setIdentifier(service.getIdentifier());
            final Class<?> component = Ut.clazz(service.getIdentifierComponent(), null);
            identity.setIdentifierComponent(component);
            /*
             * Bind sigma to identity for future usage.
             */
            identity.setSigma(service.getSigma());
            return identity;
        }), service);
    }

    static Future<ConcurrentMap<String, JsonArray>> toDictionary(final String key, final String identifier, final DictConfig dict) {
        /*
         * Params here for different situations
         */
        final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
        paramMap.add(KName.IDENTIFIER, identifier);
        final JtApp app = Ambient.getApp(key);
        if (Objects.nonNull(app)) {
            paramMap.add(KName.SIGMA, app.getSigma());
            paramMap.add(KName.APP_ID, app.getAppId());
        }
        return Ux.dictCalc(dict, paramMap);

    }
}
