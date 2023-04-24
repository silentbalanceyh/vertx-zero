package io.vertx.up.atom.worker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aeon.experiment.specification.power.KApp;
import io.aeon.experiment.specification.sch.KTimer;
import io.horizon.eon.em.scheduler.JobStatus;
import io.horizon.eon.em.scheduler.JobType;
import io.horizon.eon.info.VMessage;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Off;
import io.vertx.up.annotations.On;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.exception.web._409JobFormulaErrorException;
import io.vertx.up.exception.web._501JobOnMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Data Object to describe job detail, it stored job definition
 * 1) The definition came from scanned @Job annotation class ( each class should has one Mission )
 * 2) The definition came from JobStore interface ( the job definition may be stored into database or other
 */
public class Mission implements Serializable {
    private static final Annal LOGGER = Annal.get(Mission.class);
    /* Job status, default job is 'starting' */
    private JobStatus status = JobStatus.STARTING;
    /* Job name */
    private String name;
    /* Job type */
    private JobType type;
    /* Job code */
    private String code;
    /* Job description */
    private String comment;
    /* Whether this job is read only */
    private boolean readOnly;
    /*
     * Threshold value for timeout of worker here.
     * This parameter will be bind to current mission for set worker timeout when long time worker executed.
     * It means that the following code will be executed:
     *
     * final WorkerExecutor executor = this.vertx.createSharedWorkerExecutor(code, 1, threshold);
     *
     * For above code, the system will set the timeout parameter of current worker ( Background Job ) and it's
     * not related to scheduling instead.
     *
     * So I moved the code from `KTimer` into `Mission` here.
     *
     * There are two mode focus on this parameter calculation as:
     * 1) From programming part:
     * 2) From configuration part:
     *
     * - The default time unit is TimeUnit.SECONDS
     * - The final result should be `ns`.
     *
     * This field could not be serialized directly, you must call `timeout` to set this value
     * or the worker will use default parameters.
     **/
    @JsonIgnore
    private long threshold = Values.RANGE;
    /* Job configuration */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject metadata = new JsonObject();
    /* Job additional */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject additional = new JsonObject();

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> income;

    private String incomeAddress;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> outcome;

    private String outcomeAddress;

    /* Job reference */
    @JsonIgnore
    private Object proxy;
    /* Job begin method */
    @JsonIgnore
    private Method on;
    /* Job end method */
    @JsonIgnore
    private Method off;
    /*
     * New attribute for
     * Application Scope based on KApp specification here that belong to one KApp information
     * The attribute is as following:
     * {
     *     "name":      "application name",
     *     "ns":        "the default namespace",
     *     "language":  "the default language",
     *     "sigma":     "the uniform sigma identifier"
     * }
     *
     * Be careful that this variable will be used in Zero Extension Framework and it's based on
     * `X_APP` etc here, for programming part it's null before SVN Store connected. In future
     * version all the configuration data will be stored in integration, it means that you can
     * set any information of current Mission reference.
     * */
    @JsonIgnore
    private KApp app;

    @JsonIgnore
    private KTimer timer;

    public JobStatus getStatus() {
        return this.status;
    }

    public void setStatus(final JobStatus status) {
        this.status = status;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public JobType getType() {
        return this.type;
    }

    public void setType(final JobType type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public JsonObject getMetadata() {
        return this.metadata;
    }

    public void setMetadata(final JsonObject metadata) {
        this.metadata = metadata;
    }

    public JsonObject getAdditional() {
        return this.additional;
    }

    public void setAdditional(final JsonObject additional) {
        this.additional = additional;
    }

    public Object getProxy() {
        return this.proxy;
    }

    public void setProxy(final Object proxy) {
        this.proxy = proxy;
    }

    public Method getOn() {
        return this.on;
    }

    public void setOn(final Method on) {
        this.on = on;
    }

    public Method getOff() {
        return this.off;
    }

    public void setOff(final Method off) {
        this.off = off;
    }

    public Class<?> getIncome() {
        return this.income;
    }

    public void setIncome(final Class<?> income) {
        this.income = income;
    }

    public String getIncomeAddress() {
        return this.incomeAddress;
    }

    public void setIncomeAddress(final String incomeAddress) {
        this.incomeAddress = incomeAddress;
    }

    public Class<?> getOutcome() {
        return this.outcome;
    }

    public void setOutcome(final Class<?> outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeAddress() {
        return this.outcomeAddress;
    }

    public void setOutcomeAddress(final String outcomeAddress) {
        this.outcomeAddress = outcomeAddress;
    }

    public Mission connect(final Class<?> clazz) {
        /*
         * Here the system should connect clazz to set:
         * 1. proxy
         *    - on
         *    - off
         * 2. in/out
         *    - income
         *    - incomeAddress
         *    - outcome
         *    - outcomeAddress
         */
        final Object proxy = Ut.singleton(clazz);
        if (Objects.nonNull(proxy)) {
            /*
             * 1. proxy of class has bee initialized successfully
             * Care: The field of other instances will be bind in future after mission
             */
            this.proxy = proxy;
            /*
             * 2. @On
             *  It's required in clazz definition or here should throw exception or errors
             */
            this.on = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(On.class))
                .findFirst().orElse(null);
            Fn.out(null == this.on, _501JobOnMissingException.class,
                this.getClass(), clazz.getName());
            /*
             * Income / IncomeAddress
             */
            final Annotation on = this.on.getAnnotation(On.class);
            this.incomeAddress = this.invoke(on, "address", this::getIncomeAddress);
            this.income = this.invoke(on, "income", this::getIncome);
            if (Ut.isNil(this.incomeAddress)) {
                this.incomeAddress = null;
            }

            /*
             * 3. @Off
             * It's optional in clazz definition
             */
            this.off = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Off.class))
                .findFirst().orElse(null);
            if (Objects.nonNull(this.off)) {
                /*
                 * Outcome / OutcomeAddress
                 */
                final Annotation out = this.off.getAnnotation(Off.class);
                this.outcomeAddress = this.invoke(out, "address", this::getOutcomeAddress);
                this.outcome = this.invoke(out, "outcome", this::getOutcome);
                if (Ut.isNil(this.outcomeAddress)) {
                    this.outcomeAddress = null;
                }
            }
            if (Debugger.devJobBoot()) {
                LOGGER.info(VMessage.MISSION_JOB_OFF, this.getCode());
            }
        }
        return this;
    }

    private <T> T invoke(final Annotation annotation, final String annotationMethod,
                         final Supplier<T> supplier) {
        /*
         * Stored in database / or @Job -> config file
         */
        T reference = supplier.get();
        if (Objects.isNull(reference)) {
            /*
             * Annotation extraction
             */
            reference = Ut.invoke(annotation, annotationMethod);
        }
        return reference;
    }

    public Mission timeout(final Integer input, final TimeUnit unit) {
        if (Objects.isNull(input)) {
            this.threshold = Values.RANGE;
            return this;
        } else {
            this.threshold = unit.toNanos(input);
            return this;
        }
    }

    public long timeout() {
        if (Values.RANGE == this.threshold) {
            // The default timeout is 15 min
            return TimeUnit.MINUTES.toNanos(15);
        } else {
            return this.threshold;
        }
    }

    // ========================== KApp Information =======================
    // The bind method on KApp
    public Mission app(final KApp app) {
        this.app = app;
        return this;
    }

    public KApp app() {
        return this.app;
    }

    public Mission timer(final KTimer timer) {
        this.timer = timer;
        return this;
    }

    public KTimer timer() {
        return this.timer;
    }

    // ========================== Ensure the correct configuration =======================
    public void detectPre(final String formula) {
        if (JobType.FORMULA == this.type) {
            Fn.outWeb(Ut.isNil(formula), _409JobFormulaErrorException.class, this.getClass(), formula);
        }
    }

    @Override
    public String toString() {
        return "Mission{" +
            "status=" + this.status +
            ", name='" + this.name + '\'' +
            ", readOnly='" + this.readOnly + '\'' +
            ", type=" + this.type +
            ", code='" + this.code + '\'' +
            ", comment='" + this.comment + '\'' +
            ", metadata=" + this.metadata +
            ", additional=" + this.additional +
            ", income=" + this.income +
            ", incomeAddress='" + this.incomeAddress + '\'' +
            ", outcome=" + this.outcome +
            ", outcomeAddress='" + this.outcomeAddress + '\'' +
            ", proxy=" + this.proxy +
            ", on=" + this.on +
            ", off=" + this.off +
            ", app=" + this.app +
            ", timer=" + this.timer +
            '}';
    }
}
