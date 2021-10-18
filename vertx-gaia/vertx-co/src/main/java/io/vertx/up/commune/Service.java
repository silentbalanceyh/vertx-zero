package io.vertx.up.commune;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.exchange.BiTree;
import io.vertx.up.commune.rule.RuleUnique;

/*
 * Uniform interface for Job/Component definition
 * AbstractJob / AbstractComponent
 *
 * 「Tree Structure」
 * Component:
 * |- Adaptor: AdaptorComponent ( database )
 * ---- | - Director: AbstractDirector ( database, mission )
 * ---- | - Connector: AbstractConnector ( database, integration )
 * -------- | - Actor: AbstractActor ( database, integration, mission )
 * |- Job: AbstractJob ( database )
 * ---- | - AbstractIncome
 * ---- | - AbstractOutcome
 */
public interface Service {

    /*
     * `serviceConfig` ( SERVICE_CONFIG ) of I_SERVICE
     */
    JsonObject options();

    /*
     * `identifier`
     * `identifierComponent`
     * It's for Job / Component here
     * 1) Get identifier directly
     * 2) Check whether there exist `identifierComponent` to determine the final
     * used `identifier` instead of static
     */
    Identity identity();

    /*
     * `mappingConfig`
     * `mappingMode`
     * `mappingComponent` of I_SERVICE
     * It's also for Job / Component here
     */
    BiTree mapping();

    /*
     * `rule`
     */
    RuleUnique rule();
}
