package io.vertx.mod.jet.uca.business;

import io.horizon.exception.WebException;
import io.horizon.exception.web._501NotImplementException;
import io.horizon.spi.jet.JtComponent;
import io.horizon.uca.log.Annal;
import io.modello.specification.atom.HRule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Contract;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.atom.exchange.DConsumer;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.exception.web._400SigmaMissingException;
import io.vertx.up.specification.action.Service;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Four type components here, it is base class of
 * 「Tree Structure」
 * Component:
 * |- Adaptor: AdaptorComponent ( database )
 * ---- | - Director: AbstractDirector ( database, mission )
 * ---- | - Connector: AbstractConnector ( database, integration )
 * -------- | - Actor: AbstractActor ( database, integration, mission )
 * <p>
 * 「Not Recommend」
 * Here we do not recommend use this component directly.
 */
public abstract class AbstractComponent implements JtComponent, Service {

    // -------------- Metadata configuration ------------------
    /*
     * Could be used by sub-class directly ( XHeader contains )
     * X-Sigma      -> sigma
     * X-Lang       -> language
     * X-App-Id     -> appId
     * X-App-Key    -> appKey
     */
    @Contract
    protected transient XHeader header;  // Came from request
    /*
     *
     * Here are dict configuration
     * dict
     * - dictConfig
     * - dictComponent
     * - dictEpsilon
     *
     * The situation for dict is complex because all the sub-classes could not use
     * `Dict` directly, instead they all used `fabric` api to get `DictFabric` based on
     * dictData and dictEpsilon here.
     *
     * `DictFabric` is new structure but it could support
     * 1) One Side
     * inTo / inFrom
     * 2) Two Sides ( with mapping binding )
     * outTo / outFrom
     */
    @Contract
    protected transient DFabric fabric;

    /*
     * The four reference source came from `@Contract` injection here
     * options
     * - serviceConfig
     *
     * identity
     * - identityComponent
     * - identity
     *
     * mapping
     * - mappingConfig
     * - mappingMode
     * - mappingComponent
     *
     * rule
     * - rule
     */
    @Contract
    private transient JsonObject options;
    @Contract
    private transient Identity identity;
    @Contract
    private transient BTree mapping;
    @Contract
    private transient HRule rule;

    /*
     * There are required attribute
     * {
     *     "name": "app name",
     *     "identifier": "current identifier"
     * }
     */
    @Override
    public JsonObject options() {
        return Objects.isNull(this.options) ? new JsonObject() : this.options.copy();
    }

    @Override
    public Identity identity() {
        return this.identity;
    }

    @Override
    public BTree mapping() {
        return this.mapping;
    }

    @Override
    public HRule rule() {
        return this.rule;
    }

    // ------------ Uniform default major transfer method ------------
    /*
     * Uniform tunnel
     * 1 - sigma in XHeader is required for calling this method here
     * 2 - it means that current framework should support multi-application structure
     * */
    protected Future<ActOut> transferAsync(final ActIn request, final Function<String, Future<ActOut>> executor) {
        final String sigma = request.sigma();
        if (Ut.isNil(sigma)) {
            final WebException error = new _400SigmaMissingException(this.getClass());
            return ActOut.future(error);
        } else {
            return executor.apply(sigma);
        }
    }

    /*
     * Provide default implementation
     * 1) For standard usage, it should provide sub-class inherit structure.
     * 2) For standalone usage, it could be parent class as @Contract parent
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn actIn) {
        final WebException error = new _501NotImplementException(this.getClass());
        return Future.failedFuture(error);
    }

    // ------------ Specific Method that will be used in sub-class ------------
    /*
     * Contract for uniform reference
     * For most usage positions, it could bind current @Contract references to
     * target entity for future use.
     * Only remove `dict` in @Contract after JtComponent
     *
     * Here provide the boundary for this kind of component usage.
     * 1 - Before channel, the channel could bind dict to `Component`.
     * 2 - After component, the `Dict` should be converted to `DictFabric` instead.
     */
    protected <T> void contract(final T instance) {
        if (Objects.nonNull(instance)) {
            /*
             * Here contract `Dict` will not be support under JtComponent here
             */
            Ut.contract(instance, JsonObject.class, this.options());
            Ut.contract(instance, Identity.class, this.identity());
            Ut.contract(instance, BTree.class, this.mapping());
            Ut.contract(instance, DFabric.class, this.fabric);
            Ut.contract(instance, XHeader.class, this.header);
            Ut.contract(instance, HRule.class, this.rule);
        }
    }

    // ------------ Dictionary Structure for sub-class to call translating ------------
    /*
     * Get dict fabric
     * 1 - For each component reference, the DictFabric is unique.
     * 2 - The `Epsilon` could be bind once, in this situation, it could be put into instance
     *     to avoid created duplicated here.
     * 3 - The DictFabric must clear dictData when call `dict()` method,
     *     in most situations, it should call once instead of multi.
     *
     * For `DictFabric` usage
     * - If the component use standard fabric, it could reference `protected` member directly.
     * - If the component use new fabric, it could created based on `fabric` with new `DictEpsilon` here.
     */
    protected DFabric fabric(final JsonObject configured) {
        final ConcurrentMap<String, DConsumer> compiled = Ux.dictEpsilon(configured);
        return this.fabric.copy().epsilon(compiled);
    }

    // ------------ Get reference of Logger ------------
    /*
     * The logger of Annal here
     */
    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
