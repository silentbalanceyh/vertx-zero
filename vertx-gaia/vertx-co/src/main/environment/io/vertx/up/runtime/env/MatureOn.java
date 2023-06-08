package io.vertx.up.runtime.env;

import io.horizon.atom.common.AttrSet;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.EmDS;
import io.horizon.runtime.Macrocosm;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.boot.HMature;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;

/**
 * 专用环境变量统一入口，可直接提取核心环境变量，根据不同维度对环境变量进行分类
 * immutable on
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MatureOn implements Macrocosm {
    private static final Cc<String, HMature> CC_MATURE = Cc.openThread();

    // Cloud Connected
    public static JsonObject envPlot(final JsonObject plot) {
        final AttrSet set = AttrSet.of()
            .save(KName.CLOUD, AEON_CLOUD)                                  // AEON_CLOUD
            .save(KName.CHILD, AEON_APP)                                    // AEON_APP
            .save(KName.NAME, Z_APP)                                        // Z_APP
            .save(KName.NAMESPACE, Z_NS)                                    // Z_NS
            .saveWith(KName.LANGUAGE, Z_LANG, KWeb.ARGS.V_LANGUAGE)   // Z_LANG
            .save(KName.SIGMA, Z_SIGMA);                                    // Z_SIGMA
        // 创建拷贝
        final JsonObject plotJ = Ut.valueJObject(plot, true);
        final HMature mature = CC_MATURE.pick(MatureEnv::new, MatureEnv.class.getName());
        return mature.configure(plotJ, set);
    }

    // Restful Connected ( Multi Support )
    public static JsonObject envApi(final JsonObject api, final Integer index) {
        final AttrSet set = envServer(API_HOST, API_PORT, index);
        // 创建拷贝
        final JsonObject apiJ = Ut.valueJObject(api, true);
        final HMature mature = CC_MATURE.pick(MatureEnv::new, MatureEnv.class.getName());
        return mature.configure(apiJ, set);
    }

    // Socket Connected ( Multi Support )
    public static JsonObject envSock(final JsonObject sock, final Integer index) {
        final AttrSet set = envServer(SOCK_HOST, SOCK_PORT, index);
        // 创建拷贝
        final JsonObject sockJ = Ut.valueJObject(sock, true);
        final HMature mature = CC_MATURE.pick(MatureEnv::new, MatureEnv.class.getName());
        // 重写规则Z_API_PORTX 重写 Z_SOCK_PORTX
        final JsonObject normJ = mature.configure(sockJ, set);
        final JsonObject apiJ = envApi(new JsonObject(), index);
        if (Objects.isNull(normJ.getValue(KName.PORT))) {
            normJ.put(KName.PORT, apiJ.getValue(KName.PORT));
        }
        return normJ;
    }

    // Database Connected ( Multi Support )
    public static JsonObject envDatabase(final JsonObject database, final EmDS.Stored mode) {
        final AttrSet set;
        if (EmDS.Stored.WORKFLOW == mode) {
            // Workflow
            set = envDatabase(DBW_HOST, DBW_PORT, DBW_INSTANCE);
        } else if (EmDS.Stored.HISTORY == mode) {
            // History
            set = envDatabase(DBH_HOST, DBH_PORT, DBH_INSTANCE);
        } else {
            // Database
            set = envDatabase(DBS_HOST, DBS_PORT, DBS_INSTANCE);
        }
        // 创建拷贝
        final JsonObject databaseJ = Ut.valueJObject(database, true);
        final HMature mature = CC_MATURE.pick(MatureEnv::new, MatureEnv.class.getName());
        final JsonObject normJ = mature.configure(databaseJ, set);
        // JdbcUrl
        final String jdbcUrl = Ut.valueString(normJ, "jdbcUrl");
        final String replaced;
        if (jdbcUrl.contains(VString.DOLLAR)) {
            final JsonObject parameters = normJ.copy();
            replaced = Ut.fromExpression("`" + jdbcUrl + "`", parameters);
        } else {
            replaced = jdbcUrl;
        }
        normJ.put("jdbcUrl", replaced);
        return normJ;
    }

    // Domain Connected
    public static JsonArray envDomain(final JsonArray domainA) {
        final String domain = Ut.env(CORS_DOMAIN);
        if (Ut.isNil(domain)) {
            return domainA;
        }
        final String[] domainL = domain.split(VString.COMMA);
        if (0 == domainL.length) {
            return domainA;
        }
        // Replaced
        final JsonArray replaced = new JsonArray();
        Arrays.stream(domainL).forEach(replaced::add);
        return replaced;
    }

    private static AttrSet envDatabase(final String host, final String port, final String instance) {
        return AttrSet.of()
            .save(KName.HOSTNAME, host)
            .save(KName.PORT, port, Integer.class)
            .save(KName.INSTANCE, instance);
    }

    private static AttrSet envServer(final String host, final String port, final Integer index) {
        final String envHost;
        final String envPort;
        if (Objects.isNull(index) || VValue.IDX == index) {
            envHost = host;
            envPort = port;
        } else {
            // HOST1, HOST2
            // PORT1, PORT2
            envHost = API_HOST + index;
            envPort = API_PORT + index;
        }
        return AttrSet.of()
            .saveWith(KName.HOST, envHost, KWeb.DEPLOY.HOST)       // Z_API_HOSTX
            .save(KName.PORT, envPort, Integer.class);                     // Z_API_PORTX
    }
}
