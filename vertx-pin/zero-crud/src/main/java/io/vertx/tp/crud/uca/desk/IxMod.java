package io.vertx.tp.crud.uca.desk;

import io.aeon.experiment.specification.KJoin;
import io.aeon.experiment.specification.KModule;
import io.aeon.experiment.specification.KPoint;
import io.horizon.eon.em.scheduler.JoinMode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._404ModuleMissingException;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.horizon.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static io.vertx.tp.crud.refine.Ix.LOG;

/**
 * Wrap `envelop` here as request params
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IxMod {
    private final transient JsonObject parameters = new JsonObject();
    private transient Envelop envelop;
    private transient KModule module;
    private transient KModule connect;
    private transient WebException error;

    private IxMod(final String actor) {
        final KModule module;
        try {
            /* 2. IxModule extracting by `actor` */
            module = IxPin.getActor(actor);
            Fn.out(Objects.isNull(module), _404ModuleMissingException.class, this.getClass(), actor);
            this.module = module;
        } catch (final WebException error) {
            error.printStackTrace();
            this.error = error;
        } catch (final Throwable error) {
            error.printStackTrace();
            this.error = new _500InternalServerException(this.getClass(), error.getMessage());
        }
    }

    public static IxMod create(final String actor) {
        return new IxMod(actor);
    }

    public String cacheKey() {
        if (Objects.isNull(this.connect)) {
            return this.module.identifier();
        } else {
            return this.module.identifier() + ":" + this.connect.identifier();
        }
    }

    public boolean canJoin() {
        return Objects.nonNull(this.connect);
    }

    public boolean canTransform() {
        return Objects.nonNull(this.module.getTransform());
    }

    public Envelop envelop() {
        return this.envelop;
    }

    public User user() {
        return this.envelop.user();
    }

    public JsonObject parameters() {
        return this.parameters.copy();
    }

    // --------------- Metadata ----------------------
    public KModule module() {
        return this.module;
    }

    public KModule connect() {
        return this.connect;
    }

    public String keyPool() {
        final StringBuilder key = new StringBuilder();
        key.append(this.module.identifier());
        if (this.canJoin()) {
            key.append(this.connect.identifier());
        }
        return key.toString();
    }

    // --------------- Bind ----------------------
    public IxMod bind(final Envelop envelop) {
        this.envelop = envelop;
        final JsonObject headers = envelop.headersX();
        this.parameters.mergeIn(headers, true);
        final JsonObject parameters = envelop.body();
        if (Ut.isNotNil(parameters)) {
            this.parameters.mergeIn(parameters, true);
        }
        return this;
    }

    public IxMod connecting(final Object input) {
        if (Objects.isNull(input)) {
            return null;
        }
        /*
         * This statement must execute before connect checking to avoid
         * Returned
         */
        if (input instanceof String) {
            this.parameters.put(KName.MODULE, (String) input);
        }
        /*
         * 1. When ADD / UPDATE
         *    1.1. P1: `module` parameter is the first priority
         *    1.2. P2: When `module` has not been provided, here should be SMART processing on BODY
         *    1.3. P3: The default workflow
         *
         * 2. Other situations
         *    2.1. P1: `module` parameter is the first priority
         *    2.2. P2: The default workflow
         */
        final KJoin connect = this.module.getConnect();
        /*
         * When `KJoin` is null, it means that current module does not support any
         * extension for sub-modules, in this situation, clear the module parameters
         * because it's useless.
         */
        if (Objects.isNull(connect)) {
            return null;
        }
        KPoint target = null;
        if (input instanceof String) {
            /*
             * Connected by `module` parameters
             */
            final String module = (String) input;
            target = connect.point(module);
        } else if (input instanceof JsonObject) {
            /*
             * Connected by `JsonObject` parameters
             */
            target = connect.point((JsonObject) input);
        } else if (input instanceof JsonArray) {
            /*
             * Connected by `JsonArray` parameters
             */
            target = connect.point((JsonArray) input);
        }
        if (Objects.nonNull(target) && JoinMode.CRUD == target.modeTarget()) {
            final IxMod standBy = IxMod.create(target.getCrud()).bind(this.envelop);
            this.connect = standBy.module;
            return standBy;
        }
        return null;
    }

    // --------------- Data Processing ---------------
    /*
     * JsonArray processing
     * Here the `input` is the super set of `active`, it means that all the fields will be in `input`
     * and `active` contains the major table fields.
     */
    public JsonArray dataIn(final JsonArray input, final JsonArray active) {
        /*
         * input contains the whole data array
         * active contains the inserted X_CATEGORY only
         * Zip by
         * 1) Active -> Key
         * 2) Input -> module -> joined
         */
        final JsonArray zip = new JsonArray();
        // Issue: https://github.com/silentbalanceyh/hotel/issues/323
        this.zipData(input, active, (source, target) -> {
            // Check Target
            final JsonObject normalized;
            if (Ut.isNil(target)) {
                normalized = this.dataIn(source);
            } else {
                normalized = this.dataIn(source, target);
            }
            zip.add(normalized);
        });
        return zip;
    }

    public JsonArray dataOut(final JsonArray active, final JsonArray standBy) {

        final JsonArray zip = new JsonArray();
        this.zipData(active, standBy, (source, target) -> {
            // Check Target
            final JsonObject normalized;
            if (Ut.isNil(target)) {
                normalized = this.dataOut(source);
            } else {
                normalized = this.dataOut(source, target);
            }
            zip.add(normalized);
        });
        return zip;
    }

    /*
     * input contains two model data
     * 1) active data
     * 2) standBy data ( Only Data )
     *
     * Here the `key` came from active data and generate `joinKey = value`
     * kv put into final data
     */
    public JsonObject dataIn(final JsonObject input, final JsonObject active) {
        final KPoint point = this.pointJoin();
        final KJoin connect = this.module.getConnect();
        /*
         * 1. Joined Key
         */
        final JsonObject dataS = input.copy().mergeIn(active, true);
        connect.dataIn(dataS, point, dataS);
        /*
         * 2. Mapping Part
         */
        if (Objects.nonNull(point)) {
            final JsonObject converted = Ut.aiIn(dataS, point.synonym(), false);
            dataS.mergeIn(converted, true);
        }
        return dataS;
    }

    /*
     * 1) active data ( include `key` )
     * 2) standBy data ( include `joinKey` )
     */
    public JsonObject dataOut(final JsonObject active, final JsonObject standBy) {
        final KPoint point = this.pointJoin();
        final KJoin connect = this.module.getConnect();
        /*
         * 2. Mapping StandBy
         */
        final JsonObject standJ = standBy.copy();
        if (Objects.nonNull(point)) {
            final JsonObject converted = Ut.aiOut(standJ, point.synonym(), false);
            standJ.mergeIn(converted, true);
        }
        /*
         * 1. Joined Key
         */
        final JsonObject data = standJ.mergeIn(active, true);
        connect.dataOut(data, point, data);
        return data;
    }

    // ======================= Active / StandBy Joined ===========================
    private void zipData(final JsonArray active, final JsonArray standBy,
                         final BiConsumer<JsonObject, JsonObject> consumerFn) {
        this.zipData((sourceKey, targetKey) -> Ut.itJArray(active).forEach(sourceJ -> {
            Objects.requireNonNull(sourceKey);
            final Object value = sourceJ.getValue(sourceKey);
            if (Objects.isNull(targetKey) || Objects.isNull(value)) {
                // targetKey = null
                consumerFn.accept(sourceJ, null);
            } else {
                // targetKey != null && value != null
                final JsonObject found = Ut.elementFind(standBy, targetKey, value);
                consumerFn.accept(sourceJ, found);
            }
        }));
    }

    private void zipData(final BiConsumer<String, String> joinFn) {

        final KJoin connect = this.module.getConnect();
        // Source Key
        final String sourceKey;
        if (Objects.isNull(connect)) {
            sourceKey = KName.KEY;
        } else {
            final KPoint point = connect.getSource();
            if (Objects.isNull(point)) {
                sourceKey = KName.KEY;
            } else {
                sourceKey = Ut.isNil(point.getKeyJoin()) ? KName.KEY : point.getKeyJoin();
            }
        }
        // Target Key
        String targetKey = null;
        if (this.canJoin()) {
            final KPoint point = this.pointJoin();
            if (Objects.nonNull(point)) {
                targetKey = point.getKeyJoin();
            }
        }
        joinFn.accept(sourceKey, targetKey);
    }

    // ======================= Active Only ===========================
    /*
     * When active only mode, the synonym configuration is invalid for this kind of mode
     * It means that it's pure for Active Data Here.
     */
    public JsonObject dataIn(final JsonObject active) {
        final KPoint point = this.pointJoin();
        final KJoin connect = this.module.getConnect();
        final JsonObject condJoin = new JsonObject();
        connect.dataIn(active, point, condJoin);
        return condJoin;
    }

    public JsonObject dataOut(final JsonObject active) {
        final KPoint point = this.pointJoin();
        final KJoin connect = this.module.getConnect();
        final JsonObject condJoin = new JsonObject();
        connect.dataOut(active, point, condJoin);
        return condJoin;
    }

    public JsonObject dataCond(final JsonObject active) {
        final KPoint point = this.pointJoin();
        final KJoin connect = this.module.getConnect();
        final JsonObject condJoin = new JsonObject();
        connect.dataCond(active, point, condJoin);
        return condJoin;
    }

    public JsonObject dataCond(final JsonArray active) {
        final JsonObject condition = new JsonObject();
        Ut.itJArray(active, (item, index) -> {
            final JsonObject condEach = this.dataCond(item);
            if (Ut.isNotNil(condEach)) {
                condition.put("$" + index, condEach);
            }
        });
        return condition;
    }

    public KPoint pointJoin() {
        if (this.canJoin()) {
            final KJoin join = this.module.getConnect();
            if (Objects.isNull(join)) {
                return null;
            }
            final KPoint point = join.point(this.connect.identifier());
            LOG.Rest.info(this.getClass(), "Point = {0}, From = {1}, To = {2}",
                point, this.module.identifier(), this.connect.identifier());
            return point;
        } else {
            return null;
        }
    }

    @SafeVarargs
    public final <T> Future<T> ready(
        final T input,
        final BiFunction<T, IxMod, Future<T>>... executors) {
        // Error Checking for request building
        if (Objects.nonNull(this.error)) {
            return Future.failedFuture(this.error);
        }
        return Ix.passion(input, this, executors);
    }
}
