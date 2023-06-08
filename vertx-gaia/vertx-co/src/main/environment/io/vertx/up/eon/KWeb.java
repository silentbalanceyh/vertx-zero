package io.vertx.up.eon;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface KWeb {
    /**
     * 参数专用常量
     */
    interface ARGS {
        /*
         * Aim component used this key as RoutingContext
         * key = value
         * It means cached parameters here.
         */
        String REQUEST_CACHED = "$$__REQUEST_CACHED__$$";
        /*
         * Request web flow that will be used in `zero-jet`
         * The `Answer` reply should use this key to extract previous request data
         */
        String REQUEST_BODY = "$$__REQUEST_BODY_$$";
        /*
         * Acl data information from data region for future usage
         */
        String REQUEST_ACL = "$$__REQUEST_ACL$$";


        /*
         * Flow used here
         * DIRECT -> Flow.RESOLVER ( Direct Resolving Mime )
         */
        String MIME_DIRECT = "$$DIRECT$$";
        /*
         * Flow used here
         * IGNORE -> Flow.TYPED ( Extract by Type )
         */
        String MIME_IGNORE = "$$IGNORE$$";


        String PARAM_BODY = "$$__BODY__$$";
        String PARAM_STREAM = "$$__STREAM__$$";
        String PARAM_HEADER = "$$__HEADER__$$";


        /*
         * When criteria object merged,
         * 1) original means old condition
         * 2) matrix means new condition
         * Here are result
         * {
         *      "$ORIGINAL$":{},
         *      "": true,
         *      "$MATRIX$": {}
         * }
         */
        String TREE_ORIGINAL = "$ORIGINAL$";
        String TREE_MATRIX = "$MATRIX$";

        String TREE_L = "$L$";
        String TREE_R = "$R$";


        String V_HOLDER = "HOLDER";
        String V_CONTENT_TYPE = "application/json;charset=UTF-8";

        String V_LANGUAGE = "cn";

        String V_AUDITOR = "zero.framework";

        int V_DATA_EXPIRED = 300;
    }

    interface JOB {

        String PREFIX = "jobs";

        String NS = "zero.vertx.jobs";
    }

    interface SHARED {
        /* Deployment */
        String DEPLOYMENT = "zero.pool.deployment";
        /* Component Memory Hash Map */
        String COMPONENT = "zero.pool.component";
    }

    interface COMPONENTS {
        String WALL_MONGO = "io.vertx.mod.plugin.mongo.MongoWall";
        String AGENT_RPC = "io.vertx.up.bottle.ZeroRpcAgent";
        String AGENT_API = "io.vertx.up.bottle.ZeroApiAgent";
    }

    interface MULTI {
        /**
         * Scanned data to distinguish mode
         * 1) Only Interface Style could have the indexes key such as 0,1,2 consider as data key.
         * 2) The mode impact different flow of Envelop
         */
        ConcurrentMap<Integer, String> INDEXES = new ConcurrentHashMap<Integer, String>() {
            {
                this.put(0, "0");
                this.put(1, "1");
                this.put(2, "2");
                this.put(3, "3");
                this.put(4, "4");
                this.put(5, "5");
                this.put(6, "6");
                this.put(7, "7");
            }
        };
    }

    /**
     * 专用地址常量
     */
    interface ADDR {
        /*
         * ZeroHttpAgent <-> ZeroHttpRegistry
         * Communication address to write data/status to etcd.
         */
        String EBS_REGISTRY_START = "ZERO://MICRO/REGISTRY/START";
        /*
         * ZeroRpcAgent <-> ZeroRpcRegistry
         * Communication address to write data/status to etcd.
         */
        String EBS_IPC_START = "ZERO://MICRO/IPC/START";

        /*
         * Monitor Path of Address
         */
        String API_MONITOR = "/zero/health*";
        /*
         * Websocket Path of Address
         */
        String API_WEBSOCKET = "/api/ws";
    }

    interface DEPLOY {
        /**
         * Vertx组名称
         */
        String VERTX_GROUP = "__VERTX_ZERO__";
        /**
         * 默认部署实例数
         */
        int INSTANCES = 32;
        /**
         * 默认高可用
         */
        boolean HA = true;
        /**
         * 默认主机地址
         */
        String HOST = "0.0.0.0";
    }

    /*
     * Multi application headers, it's defined by zero.
     * In that environment, these header values are required to
     * split different `AmbientEnvironment`
     * 自定义请求头
     */
    interface HEADER {

        String PREFIX = "X-";

        String X_SESSION_ID = "X-Session-Id";
        /*
         * appId
         * appKey
         * tenantId
         * sigma
         * language
         */
        String X_APP_ID = "X-App-Id";
        String X_APP_KEY = "X-App-Key";
        String X_TENANT_ID = "X-Tenant-Id";
        String X_SIGMA = "X-Sigma";
        String X_LANG = "X-Lang";


        ConcurrentMap<String, String> PARAM_MAP = new ConcurrentHashMap<>() {
            {
                this.put(HEADER.X_APP_ID, KName.APP_ID);
                this.put(HEADER.X_APP_KEY, KName.APP_KEY);
                this.put(HEADER.X_SIGMA, KName.SIGMA);
                this.put(HEADER.X_LANG, KName.LANGUAGE);
                this.put(HEADER.X_TENANT_ID, KName.Tenant.ID);
            }
        };
    }

    interface ORDER {
        /**
         * Cors Order
         * Fix issue of frontend, this handler must be at first
         * in the latest version for request preflight
         * 1,000,000
         **/
        int CORS = 1_000_000;
        /**
         * Monitor Order
         * 1,100,000
         */
        int MONITOR = 1_100_000; // 1_000_000;
        /*
         * Time Out
         * 1,150,000
         */
        int TIMEOUT = 1_150_000;
        /**
         * Cookie Order
         * 1,200,000
         */
        int COOKIE = 1_200_000;
        /**
         * Body Order
         * 1,300,000
         */
        int BODY = 1_300_000;
        /**
         * Pattern
         * 1,400,000
         */
        int CONTENT = 1_400_000;
        /**
         * Secure
         * 1,600,000
         */
        int SESSION = 1_600_000;
        /**
         * Filter for request
         * 1,800,000
         */
        int FILTER = 1_800_000;
        /**
         * User Security
         * 1,900,000
         */
        int SECURE = 1_900_000;
        /**
         * User Authorization
         * 2,000,000
         */
        int SECURE_AUTHORIZATION = 2_000_000;
        /**
         * Sign for request ( Sign Request )
         * 3,000,000
         */
        int SIGN = 3_000_000;
        /**
         * ( Default order for event )
         * 5,000,000
         */
        int EVENT = 5_000_000;

        int EVENT_USER = 5_100_000;

        int SOCK = 5_200_000;
        /**
         * ( Default for dynamic routing )
         * 6,000,000
         */
        int DYNAMIC = 6_000_000;
        /**
         * ( Default for Module such as CRUD )
         * 10,000,000
         */
        int MODULE = 10_000_000;
    }
}
