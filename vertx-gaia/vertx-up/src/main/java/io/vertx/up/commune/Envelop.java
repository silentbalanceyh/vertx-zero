package io.vertx.up.commune;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.horizon.exception.web._500InternalServerException;
import io.modello.eon.em.EmValue;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.up.commune.envelop.Rib;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.exception.web._000HttpWebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.IndexExceedException;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Envelop implements Serializable {

    /* Basic Data for Envelop such as: Data, Error, Status */
    private final HttpStatusCode status;
    private final WebException error;
    private final JsonObject data;

    /* Additional Data for Envelop, Assist Data here. */
    private final Assist assist = new Assist();
    /* Communicate Key in Event Bus, to identify the Envelop */
    private String key;
    private Acl acl;
    /*
     * Constructor for Envelop creation, two constructor for
     * 1) Success Envelop
     * 2) Failure Envelop
     * All Envelop are private mode with non-single because it's
     * Data Object instead of tool or other reference
     */

    /**
     * @param data   input data that stored into Envelop
     * @param status http status of this Envelop
     * @param <T>    The type of stored data
     */
    private <T> Envelop(final T data, final HttpStatusCode status) {
        this.data = Rib.input(data);
        this.error = null;
        this.status = status;
    }

    /**
     * @param error input error that stored into Envelop
     */
    private Envelop(final WebException error) {
        this.status = error.getStatus();
        this.error = error;
        this.data = error.toJson();
    }

    /*
     * Static method to create new Envelop with different fast mode here.
     * 1) Success: 204 Empty Envelop ( data = null )
     * 2) Success: 200 With input data ( data = T )
     * 3) Success: XXX with input data ( data = T ) XXX means that you can have any HttpStatus
     * 4) Error: 500 Default error with description
     * 5) Error: XXX with input WebException
     * 6) Error: 500 Default with Throwable ( JVM Error )
     */
    // 204, null
    public static Envelop ok() {
        return new Envelop(null, HttpStatusCode.NO_CONTENT);
    }

    public static Envelop okJson() {
        return new Envelop(new JsonObject(), HttpStatusCode.OK);
    }

    // 200, T
    public static <T> Envelop success(final T entity) {
        return new Envelop(entity, HttpStatusCode.OK);
    }

    // xxx, T
    public static <T> Envelop success(final T entity, final HttpStatusCode status) {
        return new Envelop(entity, status);
    }

    // default error 500
    public static Envelop failure(final String message) {
        return new Envelop(new _500InternalServerException(Envelop.class, message));
    }

    // default error 500 ( JVM Error )
    public static Envelop failure(final Throwable ex) {
        if (ex instanceof WebException) {
            // Throwable converted to WebException
            return failure((WebException) ex);
        } else {
            if (ex instanceof HttpException) {
                // Http Exception, When this situation, the ex may contain WebException internal
                final Throwable actual = ex.getCause();
                if (Objects.isNull(actual)) {
                    // No Cause
                    return new Envelop(new _000HttpWebException(Envelop.class, (HttpException) ex));
                } else {
                    /*
                     * 1. Loop to search until `WebException`
                     * 2. Or HttpException without cause trace
                     */
                    return failure(actual);
                }
            } else {
                // Common JVM Exception
                return new Envelop(new _500InternalServerException(Envelop.class, ex.getMessage()));
            }
        }
    }

    // other error with WebException
    public static Envelop failure(final WebException error) {
        return new Envelop(error);
    }

    // ------------------ Above are initialization method -------------------
    /*
     * Predicate to check envelop to see whether is't valid
     * Error = null means valid
     */
    public boolean valid() {
        return null == this.error;
    }

    public WebException error() {
        return this.error;
    }

    // ------------------ Below are data part -------------------
    /* Get `data` part */
    public <T> T data() {
        return Rib.get(this.data);
    }

    /* Get `Http Body` part only */
    public JsonObject body() {
        return Rib.getBody(this.data);
    }

    public JsonObject request() {
        return this.assist.requestSmart();
    }

    /* Get `data` part by type */
    public <T> T data(final Class<T> clazz) {
        return Rib.get(this.data, clazz);
    }

    /* Get `data` part by argIndex here */
    public <T> T data(final Integer argIndex, final Class<T> clazz) {
        Fn.outBoot(!Rib.isIndex(argIndex), IndexExceedException.class, this.getClass(), argIndex);
        return Rib.get(this.data, clazz, argIndex);
    }

    /* Set value in `data` part */
    public void value(final String field, final Object value) {
        Rib.set(this.data, field, value, null);
    }

    /* Set value in `data` part ( with Index ) */
    public void value(final Integer argIndex, final String field, final Object value) {
        Rib.set(this.data, field, value, argIndex);
    }

    // ------------------ Below are response Part -------------------
    /* String */
    public String outString() {
        return this.outJson().encode();
    }

    /* Json */
    public JsonObject outJson() {
        return Rib.outJson(this.data, this.error);
    }

    /* Buffer */
    public Buffer outBuffer() {
        return Rib.outBuffer(this.data, this.error);
    }

    /* Future */
    /*
    public Future<Envelop> toFuture() {
        return Future.succeededFuture(this);
    }*/

    // ------------------ Below are Bean Get -------------------
    /* HttpStatusCode */
    public HttpStatusCode status() {
        return this.status;
    }

    /* Communicate Id */
    public Envelop key(final String key) {
        this.key = key;
        return this;
    }

    public Envelop acl(final Acl acl) {
        this.acl = acl;
        return this;
    }

    public Acl acl() {
        return this.acl;
    }

    public String key() {
        return this.key;
    }

    // ------------------ Below are JqTool part ----------------
    private void reference(final Consumer<JsonObject> consumer) {
        final JsonObject reference = Rib.getBody(this.data);
        if (Objects.nonNull(reference)) {
            consumer.accept(reference);
        }
    }

    /* JqTool Part for projection */
    public void onV(final JsonArray projection) {
        this.reference(reference -> Ux.irQV(reference, projection, false));
    }

    public void inV(final JsonArray projection) {
        this.reference(reference -> Ux.irQV(reference, projection, true));
    }

    /* JqTool Part for criteria */
    public void onH(final JsonObject criteria) {
        this.reference(reference -> Ux.irAndQH(reference, criteria, false));
    }

    public void inH(final JsonObject criteria) {
        this.reference(reference -> Ux.irAndQH(reference, criteria, true));
    }

    public void onMe(final EmValue.Bool active, final boolean app) {
        final JsonObject headerX = this.headersX();
        this.value(KName.SIGMA, headerX.getValue(KName.SIGMA));
        if (EmValue.Bool.IGNORE != active) {
            this.value(KName.ACTIVE, EmValue.Bool.TRUE == active ? Boolean.TRUE : Boolean.FALSE);
        }
        // this.value(KName.ACTIVE, active);
        if (headerX.containsKey(KName.LANGUAGE)) {
            this.value(KName.LANGUAGE, headerX.getValue(KName.LANGUAGE));
        }
        if (app) {
            this.value(KName.APP_ID, headerX.getValue(KName.APP_ID));
            this.value(KName.APP_KEY, headerX.getValue(KName.APP_KEY));
        }
    }


    public void onAcl(final Acl acl) {
        if (Objects.isNull(this.data) || Objects.isNull(acl)) {
            return;
        }
        final JsonObject aclData = acl.acl();
        if (Ut.isNotNil(aclData)) {
            this.data.put(KName.Rbac.ACL, aclData);
        }
    }

    // ------------------ Below are assist method -------------------
    /*
     * Assist Data for current Envelop, all these methods will resolve the issue
     * of EventBus splitted. Because all the request data could not be got from Worker class,
     * then the system will store some reference/data into Envelop and then after
     * this envelop passed from EventBus address, it also could keep state here.
     */
    /* Extract data from Context Map */
    public <T> T context(final String key, final Class<T> clazz) {
        return this.assist.getContextData(key, clazz);
    }

    /* Get user data from User of Context */
    public String identifier(final String field) {
        return this.assist.principal(field);
    }

    /* Get Headers */
    public MultiMap headers() {
        return this.assist.headers();
    }

    public JsonObject headersX() {
        final JsonObject headerData = new JsonObject();
        this.assist.headers().names().stream()
            /* Up case is OK */
            .filter(field -> field.startsWith(KWeb.HEADER.PREFIX)
                /* Lower case is also Ok */
                || field.startsWith(KWeb.HEADER.PREFIX.toLowerCase(Locale.getDefault())))
            /*
             * Data for header
             * X-App-Id -> appId
             * X-App-Key -> appKey
             * X-Sigma -> sigma
             */
            .forEach(field -> {
                /*
                 * Lower / Upper are both Ok
                 */
                final String found = KWeb.HEADER.PARAM_MAP.keySet()
                    .stream().filter(field::equalsIgnoreCase)
                    .findFirst().map(KWeb.HEADER.PARAM_MAP::get).orElse(null);
                if (Ut.isNotNil(found)) {
                    headerData.put(found, this.assist.headers().get(field));
                }
            });
        return headerData;
    }

    public void headers(final MultiMap headers) {
        this.assist.headers(headers);
    }

    /* Session */
    public Session session() {
        return this.assist.session();
    }

    public void session(final Session session) {
        this.assist.session(session);
    }

    /* Uri */
    public String uri() {
        return this.assist.uri();
    }

    public void uri(final String uri) {
        this.assist.uri(uri);
    }

    /* Method of Http */
    public HttpMethod method() {
        return this.assist.method();
    }

    public void method(final HttpMethod method) {
        this.assist.method(method);
    }

    /* Context Set */
    public void content(final Map<String, Object> data) {
        this.assist.context(data);
    }

    /*
     * Bind Routing Context to process Assist structure
     */
    public Envelop bind(final RoutingContext context) {
        /* Bind Context for Session / User etc. */
        this.assist.bind(context);
        final HttpServerRequest request = context.request();

        /* Http Request Part */
        this.assist.headers(request.headers());
        this.assist.uri(request.uri());
        this.assist.method(request.method());

        /* Session, User, Data */
        this.assist.session(context.session());
        this.assist.user(context.user());
        this.assist.context(context.data());

        return this;
    }

    public RoutingContext context() {
        return this.assist.reference();
    }

    /*
     * Copy information to `to`
     * return to
     */
    public Envelop to(final Envelop to) {
        if (Objects.isNull(to)) {
            return to;
        } else {
            to.method(this.method());
            to.uri(this.uri());
            to.user(this.user());
            to.session(this.session());
            to.headers(this.headers());
            /*
             * Spec
             */
            to.acl(this.acl);
            to.key(this.key);
        }
        return to;
    }

    /*
     * Copy information from `from`
     * return this;
     */
    public Envelop from(final Envelop from) {
        if (Objects.nonNull(from)) {
            this.method(from.method());
            this.uri(from.uri());
            this.user(from.user());
            this.session(from.session());
            this.headers(from.headers());
            /*
             * Spec
             */
            this.acl(from.acl());
            this.key(from.key());
        }
        return this;
    }

    // ------------------ Security Parth -------------------
    public String userId() {
        return Ux.keyUser(this.user());
    }

    public User user() {
        return this.assist.user();
    }

    public void user(final User user) {
        this.assist.user(user);
    }

    public String habitus() {
        return this.assist.principal(KName.HABITUS);
    }

    /*
     * Token Part
     */
    public String token() {
        return this.assist.principal(KName.ACCESS_TOKEN);
    }

    public String token(final String field) {
        final String jwt = this.assist.principal(KName.ACCESS_TOKEN);
        final JsonObject user = Ux.Jwt.extract(jwt);
        return user.getString(field);
    }

    @Override
    public String toString() {
        return "Envelop{" +
            "status=" + this.status +
            ", error=" + this.error +
            ", data=" + this.data +
            ", assist=" + this.assist.toString() +
            ", key='" + this.key + '\'' +
            '}';
    }
}
