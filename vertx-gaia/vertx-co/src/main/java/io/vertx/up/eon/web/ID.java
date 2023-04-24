package io.vertx.up.eon.web;

import io.vertx.up.eon.KName;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface ID {
    /*
     * Aim component used this key as RoutingContext
     * key = value
     * It means cached parameters here.
     */
    String PARAMS_CONTENT = "$$PARAM_CONTENT$$";
    /*
     * MimeFlow used here
     * DIRECT -> MimeFlow.RESOLVER ( Direct Resolving Mime )
     */
    String DIRECT = "$$DIRECT$$";
    /*
     * MimeFlow used here
     * IGNORE -> MimeFlow.TYPED ( Extract by Type )
     */
    String IGNORE = "$$IGNORE$$";

    /*
     * Request web flow that will be used in `zero-jet`
     * The `Answer` reply should use this key to extract previous request data
     */
    String REQUEST_BODY = "$$CONTEXT_REQUEST$$";
    /*
     * Acl data information from data region for future usage
     */
    String REQUEST_ACL = "$$CONTEXT_ACL$$";

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
    /*
     * For reflection
     */
    String CLASS = "_class";

    interface Addr {
        /*
         * ZeroHttpAgent <-> ZeroHttpRegistry
         * Communication address to write data/status to etcd.
         */
        String REGISTRY_START = "ZERO://MICRO/REGISTRY/START";
        /*
         * ZeroRpcAgent <-> ZeroRpcRegistry
         * Communication address to write data/status to etcd.
         */
        String IPC_START = "ZERO://MICRO/IPC/START";
        /*
         * Monitor Path of Address
         */
        String MONITOR_PATH = "/zero/health*";
    }

    /*
     * Multi application headers, it's defined by zero.
     * In that environment, these header values are required to
     * split different `AmbientEnvironment`
     */
    interface Header {

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
                this.put(Header.X_APP_ID, KName.APP_ID);
                this.put(Header.X_APP_KEY, KName.APP_KEY);
                this.put(Header.X_SIGMA, KName.SIGMA);
                this.put(Header.X_LANG, KName.LANGUAGE);
                this.put(Header.X_TENANT_ID, KName.Tenant.ID);
            }
        };
    }
}
