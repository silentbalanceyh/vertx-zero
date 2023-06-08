/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.jet.domain.tables.records;


import cn.vertxup.jet.domain.tables.IApi;
import cn.vertxup.jet.domain.tables.interfaces.IIApi;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.LocalDateTime;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IApiRecord extends UpdatableRecordImpl<IApiRecord> implements VertxPojo, IIApi {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.I_API.KEY</code>. 「key」- 接口ID
     */
    @Override
    public IApiRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.KEY</code>. 「key」- 接口ID
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.NAME</code>. 「name」- 接口名称
     */
    @Override
    public IApiRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.NAME</code>. 「name」- 接口名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.URI</code>. 「uri」- 接口路径，安全路径位于 /api 之下
     */
    @Override
    public IApiRecord setUri(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.URI</code>. 「uri」- 接口路径，安全路径位于 /api 之下
     */
    @Override
    public String getUri() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.METHOD</code>. 「method」- 接口对应的HTTP方法
     */
    @Override
    public IApiRecord setMethod(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.METHOD</code>. 「method」- 接口对应的HTTP方法
     */
    @Override
    public String getMethod() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.CONSUMES</code>. 「consumes」- 当前接口使用的客户端
     * MIME
     */
    @Override
    public IApiRecord setConsumes(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.CONSUMES</code>. 「consumes」- 当前接口使用的客户端
     * MIME
     */
    @Override
    public String getConsumes() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.PRODUCES</code>. 「produces」- 当前接口使用的服务端
     * MIME
     */
    @Override
    public IApiRecord setProduces(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.PRODUCES</code>. 「produces」- 当前接口使用的服务端
     * MIME
     */
    @Override
    public String getProduces() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.SECURE</code>. 「secure」-
     * 是否走安全通道，默认为TRUE
     */
    @Override
    public IApiRecord setSecure(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.SECURE</code>. 「secure」-
     * 是否走安全通道，默认为TRUE
     */
    @Override
    public Boolean getSecure() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.COMMENT</code>. 「comment」- 备注信息
     */
    @Override
    public IApiRecord setComment(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.COMMENT</code>. 「comment」- 备注信息
     */
    @Override
    public String getComment() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.TYPE</code>. 「type」- 通信类型，ONE-WAY /
     * REQUEST-RESPONSE / PUBLISH-SUBSCRIBE
     */
    @Override
    public IApiRecord setType(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.TYPE</code>. 「type」- 通信类型，ONE-WAY /
     * REQUEST-RESPONSE / PUBLISH-SUBSCRIBE
     */
    @Override
    public String getType() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.PARAM_MODE</code>. 「paramMode」-
     * 参数来源，QUERY / BODY / DEFINE / PATH
     */
    @Override
    public IApiRecord setParamMode(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.PARAM_MODE</code>. 「paramMode」-
     * 参数来源，QUERY / BODY / DEFINE / PATH
     */
    @Override
    public String getParamMode() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.PARAM_REQUIRED</code>. 「paramRequired」-
     * 必须参数表，一个JsonArray用于返回 400基本验证（验证Query和Path）
     */
    @Override
    public IApiRecord setParamRequired(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.PARAM_REQUIRED</code>. 「paramRequired」-
     * 必须参数表，一个JsonArray用于返回 400基本验证（验证Query和Path）
     */
    @Override
    public String getParamRequired() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.PARAM_CONTAINED</code>.
     * 「paramContained」- 必须参数表，一个JsonArray用于返回 400基本验证（验证Body）
     */
    @Override
    public IApiRecord setParamContained(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.PARAM_CONTAINED</code>.
     * 「paramContained」- 必须参数表，一个JsonArray用于返回 400基本验证（验证Body）
     */
    @Override
    public String getParamContained() {
        return (String) get(11);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.IN_RULE</code>. 「inRule」- 参数验证、转换基本规则
     */
    @Override
    public IApiRecord setInRule(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.IN_RULE</code>. 「inRule」- 参数验证、转换基本规则
     */
    @Override
    public String getInRule() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.IN_MAPPING</code>. 「inMapping」- 参数映射规则
     */
    @Override
    public IApiRecord setInMapping(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.IN_MAPPING</code>. 「inMapping」- 参数映射规则
     */
    @Override
    public String getInMapping() {
        return (String) get(13);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.IN_PLUG</code>. 「inPlug」- 参数请求流程中的插件
     */
    @Override
    public IApiRecord setInPlug(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.IN_PLUG</code>. 「inPlug」- 参数请求流程中的插件
     */
    @Override
    public String getInPlug() {
        return (String) get(14);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.IN_SCRIPT</code>. 「inScript」-
     * 【保留】参数请求流程中的脚本控制
     */
    @Override
    public IApiRecord setInScript(String value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.IN_SCRIPT</code>. 「inScript」-
     * 【保留】参数请求流程中的脚本控制
     */
    @Override
    public String getInScript() {
        return (String) get(15);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.OUT_WRITER</code>. 「outWriter」- 响应格式处理器
     */
    @Override
    public IApiRecord setOutWriter(String value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.OUT_WRITER</code>. 「outWriter」- 响应格式处理器
     */
    @Override
    public String getOutWriter() {
        return (String) get(16);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.WORKER_TYPE</code>. 「workerType」-
     * Worker类型：JS / PLUG / STD
     */
    @Override
    public IApiRecord setWorkerType(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.WORKER_TYPE</code>. 「workerType」-
     * Worker类型：JS / PLUG / STD
     */
    @Override
    public String getWorkerType() {
        return (String) get(17);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.WORKER_ADDRESS</code>. 「workerAddress」-
     * 请求发送地址
     */
    @Override
    public IApiRecord setWorkerAddress(String value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.WORKER_ADDRESS</code>. 「workerAddress」-
     * 请求发送地址
     */
    @Override
    public String getWorkerAddress() {
        return (String) get(18);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.WORKER_CONSUMER</code>.
     * 「workerConsumer」- 请求地址消费专用组件
     */
    @Override
    public IApiRecord setWorkerConsumer(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.WORKER_CONSUMER</code>.
     * 「workerConsumer」- 请求地址消费专用组件
     */
    @Override
    public String getWorkerConsumer() {
        return (String) get(19);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.WORKER_CLASS</code>. 「workerClass」- OX
     * | PLUG专用，请求执行器对应的JavaClass名称
     */
    @Override
    public IApiRecord setWorkerClass(String value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.WORKER_CLASS</code>. 「workerClass」- OX
     * | PLUG专用，请求执行器对应的JavaClass名称
     */
    @Override
    public String getWorkerClass() {
        return (String) get(20);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.WORKER_JS</code>. 「workerJs」- JS
     * 专用，JavaScript路径：runtime/workers/&lt;app&gt;/下的执行器
     */
    @Override
    public IApiRecord setWorkerJs(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.WORKER_JS</code>. 「workerJs」- JS
     * 专用，JavaScript路径：runtime/workers/&lt;app&gt;/下的执行器
     */
    @Override
    public String getWorkerJs() {
        return (String) get(21);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.SERVICE_ID</code>. 「serviceId」- 关联的服务ID
     */
    @Override
    public IApiRecord setServiceId(String value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.SERVICE_ID</code>. 「serviceId」- 关联的服务ID
     */
    @Override
    public String getServiceId() {
        return (String) get(22);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public IApiRecord setSigma(String value) {
        set(23, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return (String) get(23);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public IApiRecord setLanguage(String value) {
        set(24, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(24);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public IApiRecord setActive(Boolean value) {
        set(25, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(25);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public IApiRecord setMetadata(String value) {
        set(26, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.METADATA</code>. 「metadata」- 附加配置数据
     */
    @Override
    public String getMetadata() {
        return (String) get(26);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public IApiRecord setCreatedAt(LocalDateTime value) {
        set(27, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(27);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public IApiRecord setCreatedBy(String value) {
        set(28, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(28);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public IApiRecord setUpdatedAt(LocalDateTime value) {
        set(29, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(29);
    }

    /**
     * Setter for <code>DB_ETERNAL.I_API.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public IApiRecord setUpdatedBy(String value) {
        set(30, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.I_API.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(30);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IIApi from) {
        setKey(from.getKey());
        setName(from.getName());
        setUri(from.getUri());
        setMethod(from.getMethod());
        setConsumes(from.getConsumes());
        setProduces(from.getProduces());
        setSecure(from.getSecure());
        setComment(from.getComment());
        setType(from.getType());
        setParamMode(from.getParamMode());
        setParamRequired(from.getParamRequired());
        setParamContained(from.getParamContained());
        setInRule(from.getInRule());
        setInMapping(from.getInMapping());
        setInPlug(from.getInPlug());
        setInScript(from.getInScript());
        setOutWriter(from.getOutWriter());
        setWorkerType(from.getWorkerType());
        setWorkerAddress(from.getWorkerAddress());
        setWorkerConsumer(from.getWorkerConsumer());
        setWorkerClass(from.getWorkerClass());
        setWorkerJs(from.getWorkerJs());
        setServiceId(from.getServiceId());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setActive(from.getActive());
        setMetadata(from.getMetadata());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends IIApi> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached IApiRecord
     */
    public IApiRecord() {
        super(IApi.I_API);
    }

    /**
     * Create a detached, initialised IApiRecord
     */
    public IApiRecord(String key, String name, String uri, String method, String consumes, String produces, Boolean secure, String comment, String type, String paramMode, String paramRequired, String paramContained, String inRule, String inMapping, String inPlug, String inScript, String outWriter, String workerType, String workerAddress, String workerConsumer, String workerClass, String workerJs, String serviceId, String sigma, String language, Boolean active, String metadata, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(IApi.I_API);

        setKey(key);
        setName(name);
        setUri(uri);
        setMethod(method);
        setConsumes(consumes);
        setProduces(produces);
        setSecure(secure);
        setComment(comment);
        setType(type);
        setParamMode(paramMode);
        setParamRequired(paramRequired);
        setParamContained(paramContained);
        setInRule(inRule);
        setInMapping(inMapping);
        setInPlug(inPlug);
        setInScript(inScript);
        setOutWriter(outWriter);
        setWorkerType(workerType);
        setWorkerAddress(workerAddress);
        setWorkerConsumer(workerConsumer);
        setWorkerClass(workerClass);
        setWorkerJs(workerJs);
        setServiceId(serviceId);
        setSigma(sigma);
        setLanguage(language);
        setActive(active);
        setMetadata(metadata);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised IApiRecord
     */
    public IApiRecord(cn.vertxup.jet.domain.tables.pojos.IApi value) {
        super(IApi.I_API);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setUri(value.getUri());
            setMethod(value.getMethod());
            setConsumes(value.getConsumes());
            setProduces(value.getProduces());
            setSecure(value.getSecure());
            setComment(value.getComment());
            setType(value.getType());
            setParamMode(value.getParamMode());
            setParamRequired(value.getParamRequired());
            setParamContained(value.getParamContained());
            setInRule(value.getInRule());
            setInMapping(value.getInMapping());
            setInPlug(value.getInPlug());
            setInScript(value.getInScript());
            setOutWriter(value.getOutWriter());
            setWorkerType(value.getWorkerType());
            setWorkerAddress(value.getWorkerAddress());
            setWorkerConsumer(value.getWorkerConsumer());
            setWorkerClass(value.getWorkerClass());
            setWorkerJs(value.getWorkerJs());
            setServiceId(value.getServiceId());
            setSigma(value.getSigma());
            setLanguage(value.getLanguage());
            setActive(value.getActive());
            setMetadata(value.getMetadata());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public IApiRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
