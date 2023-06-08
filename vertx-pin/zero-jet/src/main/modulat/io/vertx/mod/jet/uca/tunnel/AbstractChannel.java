package io.vertx.mod.jet.uca.tunnel;

import io.horizon.spi.jet.JtChannel;
import io.horizon.spi.jet.JtComponent;
import io.horizon.uca.log.Annal;
import io.modello.specification.HRecord;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.jet.error._501ChannelErrorException;
import io.vertx.mod.jet.monitor.JtMonitor;
import io.vertx.mod.jet.refine.Jt;
import io.vertx.up.annotations.Contract;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.atom.exchange.DSetting;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.Envelop;
import io.vertx.up.specification.action.Commercial;
import io.vertx.up.uca.cache.RapidKey;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract channel
 * reference matrix
 * Name                 Database            Integration         Mission
 * AdaptorChannel       Yes                 No                  No
 * ConnectorChannel     Yes                 Yes                 No
 * DirectorChannel      Yes                 No                  Yes
 * ActorChannel         Yes                 Yes                 Yes
 * <p>
 * For above support list, here are some rules:
 * 1) Request - Response MODE, Client send request
 * 2) Publish - Subscribe MODE, Server send request
 * <p>
 * For common usage, it should use AdaptorChannel instead of other three types; If you want to send
 * request to third part interface ( API ), you can use ConnectorChannel instead of others.
 * <p>
 * The left two: ActorChannel & DirectorChannel are Background task in zero ( Job Support ), the
 * difference between them is that whether the channel support Integration.
 * <p>
 * The full feature of channel should be : ActorChannel
 */
public abstract class AbstractChannel implements JtChannel {

    private final transient JtMonitor monitor = JtMonitor.create(this.getClass());
    /* This field will be injected by zero directly from backend */
    @Contract
    private transient Commercial commercial;
    @Contract
    private transient Mission mission;
    /*
     * In `Job` mode, the dictionary may came from `JobIncome`.
     * In `Api` mode, the dictionary is null reference here.
     */
    @Contract
    private transient ConcurrentMap<String, JsonArray> dictionary;

    @Override
    public Future<Envelop> transferAsync(final Envelop envelop) {
        /*
         * Build record and init
         */
        final Class<?> recordClass = this.commercial.recordComponent();
        /*
         * Build component and init
         */
        final Class<?> componentClass = this.commercial.businessComponent();
        if (Objects.isNull(componentClass)) {
            /*
             * null class of component
             */
            return Future.failedFuture(new _501ChannelErrorException(this.getClass(), null));
        } else {
            return this.createRequest(envelop, recordClass).compose(request -> {
                /*
                 * Create new component here
                 * It means that Channel/Component must contains new object
                 * Container will create new Channel - Component to process request
                 * Instead of singleton here.
                 *  */
                final JtComponent component = Ut.instance(componentClass);
                if (Objects.nonNull(component)) {
                    this.monitor.componentHit(componentClass, recordClass);
                    /*
                     * Initialized first and then
                     */
                    Ux.debug();
                    /*
                     * Options without `mapping` here
                     */
                    return this.initAsync(component, request)
                        /*
                         * Contract here
                         * 1) Definition in current channel
                         * 2) Data came from request ( XHeader )
                         */
                        .compose(initialized -> Anagogic.componentAsync(component, this.commercial, this::createFabric))
                        .compose(initialized -> Anagogic.componentAsync(component, envelop))
                        /*
                         * Children initialized
                         */
                        .compose(initialized -> component.transferAsync(request))
                        /*
                         * Response here for future custom
                         */
                        .compose(actOut -> this.createResponse(actOut, envelop))
                        /*
                         * Otherwise;
                         */
                        .otherwise(Ux.otherwise());
                } else {
                    /*
                     * singleton singleton error
                     */
                    return Future.failedFuture(new _501ChannelErrorException(this.getClass(), componentClass.getName()));
                }
            });
        }
    }

    private Future<Envelop> createResponse(final ActOut actOut, final Envelop envelop) {
        return Ux.future(actOut.envelop(this.commercial.mapping()).from(envelop));
    }

    /*
     * Switcher `dictionary` here for usage
     * 1) When `Job`, assist data may be initialized before.
     * 2) When `Api`, here will initialize assist data.
     * 3) Finally the data will bind to request
     */
    private Future<ActIn> createRequest(final Envelop envelop, final Class<?> recordClass) {
        /*
         * Data object, could not be singleton
         *  */
        final HRecord definition = Ut.instance(recordClass);
        /*
         * First step for channel
         * Initialize the `ActIn` object and reference
         */
        final ActIn request = new ActIn(envelop);
        request.bind(this.commercial.mapping());
        request.connect(definition);

        return Ux.future(request);
    }

    private Future<DFabric> createFabric() {
        /*
         * Dict configuration
         */
        final DSetting dict = this.commercial.dict();
        if (Objects.isNull(this.dictionary)) {
            final String appKey = this.commercial.app();
            final String identifier = this.commercial.identifier();
            return Jt.toDictionary(appKey, RapidKey.DIRECTORY, identifier, dict).compose(dictionary -> {
                /*
                 * Bind dictionary to current dictionary reference
                 */
                this.dictionary = dictionary;
                return Ux.future(DFabric.create().dictionary(dictionary).epsilon(dict.getEpsilon()));
            });
        } else {
            return Ux.future(DFabric.create().dictionary(this.dictionary).epsilon(dict.getEpsilon()));
        }
    }

    /*
     * Initialize component
     */
    public abstract Future<Boolean> initAsync(JtComponent component, ActIn request);

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    // ------------- Rename configuration object -------------
    /*
     * Get service definition from `Commercial`
     */
    protected Commercial commercial() {
        return this.commercial;
    }

    /*
     * Get job definition from `Mission`
     */
    protected Mission mission() {
        return this.mission;
    }
}
