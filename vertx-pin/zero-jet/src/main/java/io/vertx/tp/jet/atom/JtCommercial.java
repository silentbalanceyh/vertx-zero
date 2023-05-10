package io.vertx.tp.jet.atom;

import cn.vertxup.jet.domain.tables.pojos.IService;
import io.horizon.eon.em.Environment;
import io.horizon.spi.environment.Ambient;
import io.modello.specification.atom.HUnique;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.cv.JtKey;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.exchange.BTree;
import io.vertx.up.commune.exchange.DSetting;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.container.ChannelType;
import io.vertx.up.specification.action.Commercial;
import io.vertx.up.util.Ut;

import java.util.Objects;

/*
 * Another data structure for bridge
 * 1) Api + Service
 * 2) Job + Service
 *
 * It means that serviceComponent could be consumed by different entrance such as:
 * 1) Request-Response ( IApi )
 * 2) Publish-Subscribe ( IJob )
 */
@SuppressWarnings("unchecked")
public abstract class JtCommercial implements Commercial {
    /*
     * Environment selection, the default should be `Production`,
     * It means that the code logical is correct.
     */
    private transient Environment environment = Environment.Production;
    private transient IService service;
    /*
     * Shared data structure for
     * 1) JtApp ( application data )
     */
    private transient JtApp app;
    private transient JtConfig config;

    JtCommercial() {
    }

    JtCommercial(final IService service) {
        this.service = service;
    }

    public <T extends JtCommercial> T bind(final JtConfig config) {
        this.config = config;
        return (T) this;
    }

    public <T extends JtCommercial> T bind(final String appId) {
        this.app = Ambient.getApp(appId);
        return (T) this;
    }

    /*
     * Public interface to return `IService` reference
     */
    public IService service() {
        return this.service;
    }

    /*
     * Sub class used method for some processing
     */
    protected JtApp getApp() {
        return this.app;
    }

    protected JtConfig getConfig() {
        return this.config;
    }

    /*
     * Each sub class must set implementation of this method here.
     */
    public abstract String key();

    @Override
    public ChannelType channelType() {
        return Ut.toEnum(this.service::getChannelType, ChannelType.class, ChannelType.ADAPTOR);
    }

    @Override
    public Class<?> channelComponent() {
        return Jt.toChannel(this.service::getChannelComponent, this.channelType());
    }

    @Override
    public Class<?> businessComponent() {
        return Ut.clazz(this.service.getServiceComponent());
    }

    @Override
    public Class<?> recordComponent() {
        return Ut.clazz(this.service.getServiceRecord());
    }

    @Override
    public Database database() {
        return Jt.toDatabase(this.service::getConfigDatabase, this.app.getSource());
    }

    @Override
    public HUnique rule() {
        return Jt.toRule(this.service);
    }

    @Override
    public Integration integration() {
        final Integration integration = Jt.toIntegration(this.service);
        if (Environment.Mockito == this.environment) {
            /*
             * When pre-release here, the integration should be connected to actual
             * environment, in this kind of situation, you can call mock JtJob `mockOn`
             * to turn on `debug` options in integration, but it require the environment
             * to be `Mockito` instead of others.
             * Involve environment concept to split development/testing/production
             */
            integration.mockOn();
        }
        return integration;
    }

    public JtCommercial bind(final Environment environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public DSetting dict() {
        return Jt.toDict(this.service);
    }

    @Override
    public BTree mapping() {
        return Jt.toMapping(this.service);
    }

    @Override
    public Identity identity() {
        return Jt.toIdentity(this.service);
    }


    @Override
    public String app() {
        return this.app.getAppId();
    }


    /*
     * Non - Interface method here.
     */
    @Override
    public String identifier() {
        return this.service.getIdentifier();
    }

    // ---------- Basic Json
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JtUri)) {
            return false;
        }
        final JtUri jtUri = (JtUri) o;
        return this.key().equals(jtUri.key());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key());
    }

    @Override
    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        /* key data */
        data.put(JtKey.Delivery.KEY, this.key());

        /* service, config */
        final JsonObject serviceJson = Ut.serializeJson(this.service());
        data.put(JtKey.Delivery.SERVICE, serviceJson);
        data.put(JtKey.Delivery.CONFIG, (JsonObject) Ut.serializeJson(this.config));

        /* appId */
        data.put(JtKey.Delivery.APP_ID, this.app.getAppId());
        /* Reflection */
        data.put(KName.__.CLASS, this.getClass().getName());
        return data;
    }

    @Override
    public void fromJson(final JsonObject data) {
        /*
         * service, config
         */
        this.service = Ut.deserialize(data.getJsonObject(JtKey.Delivery.SERVICE), IService.class);
        this.config = Ut.deserialize(data.getJsonObject(JtKey.Delivery.CONFIG), JtConfig.class);
        /*
         * application id
         */
        final String appId = data.getString(JtKey.Delivery.APP_ID);
        this.app = Ambient.getApp(appId);
    }
}
