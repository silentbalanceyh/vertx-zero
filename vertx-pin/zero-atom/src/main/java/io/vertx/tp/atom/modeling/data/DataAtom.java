package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.cv.AoMsg;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.phantom.AoPerformer;
import io.vertx.up.commune.rule.RuleUnique;
import io.vertx.up.fn.Fn;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom {

    private transient final AoPerformer performer;
    private transient final Model model;

    private transient final String identifier;
    private transient final String unique;
    private transient RuleUnique rule;

    private DataAtom(final String appName,
                     final String identifier,
                     final String unique) {
        /* 当前模型的ID */
        this.identifier = identifier;
        /* Performer池化（每个App不一样）*/
        this.performer = AoPerformer.getInstance(appName);
        /* 构造当前模型的唯一值，从外置传入 */
        this.unique = unique;
        this.model = Fn.pool(AoCache.POOL_MODELS, unique,
                () -> this.performer.fetchModel(identifier));
        /* LOG: 日志处理 */
        Ao.infoAtom(this.getClass(), AoMsg.DATA_ATOM, unique, this.model.toJson().encode());
    }

    public static DataAtom get(final String appName,
                               final String identifier) {
        /*
         * 唯一ID，主要是内置构造函数用
         */
        final String unique = Model.namespace(appName) + "-" + identifier;
        return Fn.pool(AoCache.POOL_ATOM, unique, () -> new DataAtom(appName, identifier, unique));
    }

    /**
     * 返回当前 Model 中的所有属性集
     */
    Set<String> attributes() {
        return this.model.getAttributes().stream()
                .map(MAttribute::getName)
                .collect(Collectors.toSet());
    }


    public Model getModel() {
        return this.model;
    }

    // ------------ 标识规则 ----------

    /**
     * 返回当前模型中的标识规则
     */
    public RuleUnique rule() {
        return this.model.getUnique();
    }

    /*
     * 直接返回当前模型连接的标识规则（第二标识规则）
     */
    public RuleUnique ruleDirect() {
        return this.rule;
    }

    /*
     * 1）先检索连接的标识规则：Slave
     * 2）再检索存储的标识规则：Master
     */
    public RuleUnique ruleSmart() {
        if (Objects.nonNull(this.rule)) {
            return this.rule;
        } else {
            return this.model.getUnique();
        }
    }

    public void connect(final RuleUnique channelRule) {
        this.rule = channelRule;
    }
    // ------------ 属性检查的特殊功能，收集相关属性 ----------

    public Boolean isTrack() {
        final Boolean result = this.model.getModel().getIsTrack();
        return Objects.isNull(result) ? Boolean.FALSE : Boolean.TRUE;
    }

    /*
     * 解决空指针问题，
     * isTrack
     * isSyncIn
     * isSyncOut
     * 三个字段可能没有值
     */
    public Set<String> auditTrack() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsTrack();
            return Objects.isNull(result) ? Boolean.FALSE : result;
        });
    }

    public Set<String> ignoreTrack() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsTrack();
            return Objects.isNull(result) ? Boolean.TRUE : !result;
        });
    }

    public Set<String> auditIn() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsSyncIn();
            return Objects.isNull(result) ? Boolean.FALSE : result;
        });
    }

    public Set<String> ignoreIn() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsSyncIn();
            return Objects.isNull(result) ? Boolean.TRUE : !result;
        });
    }

    public Set<String> auditOut() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsSyncOut();
            return Objects.isNull(result) ? Boolean.FALSE : result;
        });
    }

    public Set<String> ignoreOut() {
        return this.audit(attr -> {
            final Boolean result = attr.getIsSyncOut();
            return Objects.isNull(result) ? Boolean.TRUE : !result;
        });
    }

    public Class<?> dateField(final String field) {
        return this.model.types().get(field);
    }

    public boolean isDateField(final String field) {
        return this.dateFields().containsKey(field);
    }

    /*
     * 属性类型
     */
    public ConcurrentMap<String, Class<?>> types() {
        return this.model.types();
    }

    private Set<String> audit(final Predicate<MAttribute> predicate) {
        return this.model.getAttributes().stream()
                .filter(predicate)
                .map(MAttribute::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    /*
     * 时间格式处理
     */
    private ConcurrentMap<String, Class<?>> dateFields() {
        final ConcurrentMap<String, Class<?>> dataFields = new ConcurrentHashMap<>();
        this.model.types().forEach((field, type) -> {
            if (LocalDateTime.class == type || LocalDate.class == type || LocalTime.class == type || Date.class == type) {
                dataFields.put(field, type);
            }
        });
        return dataFields;
    }

    // ------------ 返回特殊元数据（包域） ----------
    /* 返回当前记录关联的 identifier */
    public String identifier() {
        return this.identifier;
    }

    public String sigma() {
        return this.getModel().getModel().getSigma();
    }

    /* 返回当前记录对应的名空间 */
    String namespace() {
        return this.model.getModel().getNamespace();
    }

    /* 返回当前记录专用的 sigma */


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataAtom)) {
            return false;
        }
        final DataAtom atom = (DataAtom) o;
        return this.unique.equals(atom.unique);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unique);
    }
}
