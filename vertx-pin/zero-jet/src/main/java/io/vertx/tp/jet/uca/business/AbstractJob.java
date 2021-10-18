package io.vertx.tp.jet.uca.business;

import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.tp.optic.environment.Ambient;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Service;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.exchange.BiTree;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.commune.rule.RuleUnique;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Abstract Service
 */
public abstract class AbstractJob implements Service {

    /*
     * dict
     * - dictConfig
     * - dictComponent
     * - dictEpsilon
     */
    protected transient DiFabric fabric;

    /*
     * The four reference source came from Service instance here
     * dict
     * - dictConfig
     * - dictComponent
     * - dictEpsilon
     *
     * identity
     * - identityComponent
     * - identity
     *
     * options
     * - serviceConfig
     *
     * mapping
     * - mappingConfig
     * - mappingMode
     * - mappingComponent
     */
    protected DiSetting dict() {
        final DiSetting dict = Jt.toDict(this.service());
        if (Objects.isNull(this.fabric)) {
            this.fabric = DiFabric.create().epsilon(dict.getEpsilon());
        }
        return dict;
    }

    @Override
    public BiTree mapping() {
        return Jt.toMapping(this.service());
    }

    @Override
    public Identity identity() {
        return Jt.toIdentity(this.service());
    }

    @Override
    public RuleUnique rule() {
        return Jt.toRule(this.service());
    }

    @Override
    public JsonObject options() {
        final IService service = this.service();
        final JtApp app = Ambient.getApp(service.getSigma());
        return Jt.toOptions(app, service);
    }

    /*
     * Get `IService` reference here.
     */
    protected IService service() {
        final JsonObject metadata = this.mission().getMetadata();
        return Ut.deserialize(metadata.getJsonObject(KName.SERVICE), IService.class);
    }

    /*
     * All `Job` sub-class must implement this method to get `Mission` object
     * This component configuration are all created by `Mission` instead of
     * channel @Contract.
     */
    protected abstract Mission mission();

    // ----------- Database / Integration --------

    /*
     * 1. Get database reference ( Database )
     * 2. Get dao reference ( OxDao )
     * 3. Get data atom reference ( DataAtom )
     */
    protected Database database() {
        return Jt.toDatabase(this.service());
    }

    protected Integration integration() {
        return Jt.toIntegration(this.service());
    }

    /*
     * Under way processing based on `identifier`
     */
    protected Future<Refer> underway(final String identifier) {
        /*
         * Parameters
         */
        final String key = this.service().getSigma();
        return Jt.toDictionary(key, identifier, this.dict()).compose(dictionary -> {
            this.fabric.dictionary(dictionary);
            /*
             * Chain 引用
             */
            final Refer refer = new Refer();
            refer.add(dictionary);
            return Ux.future(refer);
        });
    }

    // ----------- Logger component --------------
    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
