package io.horizon.specification.modeler;

import io.horizon.eon.VString;
import io.horizon.eon.runtime.VEnv;
import io.horizon.specification.meta.typed.TJson;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HApp extends Serializable, TJson {
    /*
     * Default namespace value based on appName
     * Modular belong to `Origin X Engine`
     * It's critical extension in zero framework, it could do dynamic modular on DDL in database
     * Also you could provide your only implementation to replace some configuration.
     */
    static String ns(final String appName) {
        return Ut.isNil(appName) ? null : Ut.fromMessage(VEnv.APP.NS, appName);
    }

    static String ns(final String appName, final String identifier) {
        return ns(appName) + VString.DASH + identifier;
    }

    /* Uniform Model identifier */
    String identifier();

    /*
     * Uniform namespace, when you define the `ns` in multi-app structure, you must provide `namespace` for each
     * standalone namespace, in this kind of situation the static model could be shared in one database, but the
     * dynamic model must be standalone.
     *
     * Store in one database is only for platform interface to manage data uniform, for single application it should
     * be deployed in standalone docker:  Docker App + Docker Database, it means that the application is running
     * in a environment with:  appId <-> sigma ( One to One )
     *
     * This part will be re-design or re-set in future when the requirement move to platform admin, in current version,
     * the ( 1 - 1 ) structure could be used.
     *
     * The common system architecture should be:
     * 1. Tenant identifier: based on `sigma`
     * 2. Application identifier: based on `appId/appKey`
     *    - appId: the system identifier with public information
     *    - appKey: the system identifier with private or sensitive information
     * 3. Each `sigma` may contain multi `appId`
     *    Sigma -> App1, App2, App3
     *
     * The scope of data of platform / application
     *
     * 1) X_MENU                        ---->           AppId
     * 2) X_TABULAR
     *    X_CATEGORY                    ---->           AppId / Sigma
     * 3) X_ACTIVITY_RULE               ---->           AppId ( namespace )
     * 4) M_MODEL                       ---->           AppId ( namespace )
     * 4) I_API / I_JOB / I_SERVICE     ---->           AppId ( namespace )
     */
    String namespace();

    /*
     * Uniform File Path for initialize
     */
    String file();
}
