package io.vertx.mod.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.aeon.experiment.shape.AbstractHModel;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.program.HArk;
import io.modello.dynamic.modular.apply.AoDefault;
import io.modello.eon.em.EmModel;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HReference;
import io.modello.specification.atom.HRule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.element.DataKey;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Ox专用模型：
 * 1. 包含一个MModel
 * 2. 包含该Model对应的MAttribute集合
 * 3. 包含多个Schema
 */
public class DataModel extends AbstractHModel implements Model {
    private final transient Set<Schema> schemata = new HashSet<>();
    /* 所有关联的Entity的ID */
    private final transient Set<MJoin> joins = new HashSet<>();
    /* 延迟填充 */
    private final ConcurrentMap<String, MAttribute> attributes = new ConcurrentHashMap<>();
    /* 当前Model关联的模型 */
    private transient MModel model;
    /* 当前Model的主键信息 */
    private transient DataKey key;

    public DataModel(final HArk ark) {
        super(ark);
        /*
         * Call `initialize()` method again
         * Fix issue:
         * Caused by: java.lang.NullPointerException: null
                at io.vertx.mod.builtin.modeling.atom.DataModel.dbAttributes(DataModel.java:126)
         * Because parent class constructor is initialized first, it means that you must do as following:
         * 1) AbstractHModel create
         * 2) Current Object create
         * 3) Do initialize
         *    -- loadAttribute
         *    -- loadRule
         *    -- loadMarker
         *    -- loadReference
         * 4) When you call fromJson, this method still need to be called.
         */
    }

    // =================== Abstract Class Method ====================
    @Override
    protected ConcurrentMap<String, HAttribute> loadAttribute() {
        final ConcurrentMap<String, HAttribute> attrMap = new ConcurrentHashMap<>();
        /* 读取所有 MAttribute */
        this.dbAttributes().forEach(attribute -> {
            /*
             * 根据 source, sourceField 读取 MField 来执行
             * AoAttribute的构造
             * */
            final Schema schema = this.schema(attribute.getSource());
            final MField field = Objects.isNull(schema) ? null : schema.getField(attribute.getSourceField());

            Cc.pool(attrMap, attribute.getName(), () -> new AtomAttribute(attribute, field));
        });
        return attrMap;
    }

    @Override
    public HReference reference() {
        return new AtomReference(this, this.ark);
    }

    @Override
    protected boolean trackable() {
        final Boolean isTrack = this.dbModel().getIsTrack();
        return Objects.isNull(isTrack) ? Boolean.TRUE : isTrack;
    }

    @Override
    protected HRule loadRule() {
        final String content = this.model.getRuleUnique();
        if (Ut.isNotNil(content)) {
            return HRule.of(content);
            // return Ut.deserialize(content, KRuleUnique.class);
        } else {
            return null;
        }
    }

    // =================== Extension Api ====================
    @Override
    public MModel dbModel() {
        return this.model;
    }

    @Override
    public Schema schema(final String identifier) {
        return Ut.isNil(identifier) ? null : this.schemata.stream()
            .filter(schema -> identifier.equals(schema.identifier()))
            .findFirst().orElse(null);
    }


    @Override
    public Set<Schema> schema() {
        return this.schemata;
    }

    @Override
    public DataKey key() {
        return this.key;
    }

    @Override
    public void key(final DataKey key) {
        this.key = key;
    }

    @Override
    public EmModel.Type type() {
        final String typeStr = this.model.getType();
        final EmModel.Type type = Ut.toEnum(typeStr, EmModel.Type.class);
        return null == type ? EmModel.Type.DIRECT : type;
    }

    @Override
    public Set<MAttribute> dbAttributes() {
        return new HashSet<>(this.attributes.values());
    }

    @Override
    public MAttribute dbAttribute(final String attributeName) {
        return this.attributes.get(attributeName);
    }

    @Override
    public Model bind(final Set<Schema> schemas) {
        // 从Json文件中读取时：需要检查joins
        if (!this.joins.isEmpty()) {
            // 桥接
            Bridge.connect(this, schemas);
            this.initialize();
        }
        return this;
    }

    @Override
    public Set<MJoin> dbJoins() {
        return this.joins;
    }

    @Override
    public void connect(final String key) {
        // 修改 MModel 主键
        this.model.setKey(key);
        // 修改 MAttribute 关联主键
        this.attributes.values().forEach(attribute -> attribute.setModelId(key));
    }

    @Override
    public void bindDirect(final Set<Schema> schemas) {
        // 从数据库中读取时：schemas 和 foundSchemas 相等
        Bridge.connect(this, schemas);
    }

    // =================== Serialization Method ====================
    @Override
    public void fromJson(final JsonObject json) {
        // 第二次
        this.attributes.clear();
        this.joins.clear();
        this.schemata.clear();
        // 直接从JsonObject中读取数据
        final JsonObject model = json.getJsonObject(KName.MODEL);
        AoDefault.model().applyJson(model);
        // 针对 ruleUnique
        {
            final Object uniqueRef = model.getValue(KName.RULE_UNIQUE);
            if (uniqueRef instanceof final JsonObject content) {
                /*
                 * 反序列化成 RuleUnique
                 */
                this.unique = HRule.of(content);
                model.put(KName.RULE_UNIQUE, content.encode());
            }
        }
        {
            this.model = Ut.deserialize(model, MModel.class);
            // 直接设置名空间
            final String namespace = this.ark.app().ns();
            this.model.setNamespace(namespace);
            this.identifier = this.model.getIdentifier();
        }

        // 填充属性
        final JsonArray attributes = json.getJsonArray(KName.Modeling.ATTRIBUTES);
        Ut.itJArray(attributes, (attribute, index) -> {
            // 设置attribute的默认值
            AoDefault.attribute().mount(this.model).applyJson(attribute);
            final MAttribute attributeObj = Ut.deserialize(attribute, MAttribute.class);
            if (Ut.isNotNil(attributeObj.getName())) {
                this.attributes.put(attributeObj.getName(), attributeObj);
            }
        });
        // 读取join，并且通过 join 计算关系
        final JsonArray joins = json.getJsonArray(KName.Modeling.JOINS);
        Ut.itJArray(joins, (join, index) -> {
            // 设置join的值
            AoDefault.join().mount(this.model).applyJson(join);
            {
                final MJoin joinObj = Ut.deserialize(join, MJoin.class);
                this.joins.add(joinObj);
            }
        });
        // 读取schemata
        final JsonArray schemata = json.getJsonArray(KName.Modeling.SCHEMATA);
        if (null != schemata) {
            /* 在填充 Schema 的过程中直接处理 DataKey */
            final String namespace = this.ark.app().ns();
            Ut.itJArray(schemata, (schema, index) -> {
                final Schema schemaObj = new DataSchema(namespace);
                schemaObj.fromJson(schema);
                this.schemata.add(schemaObj);
            });
            Bridge.connect(this, this.ark.cached(this.identifier));
            this.initialize();
        }
    }

    @Override
    public JsonObject toJson() {
        final JsonObject content = new JsonObject();

        // 模型处理
        final JsonObject model = Ut.serializeJson(this.model);
        // 针对Unique
        {
            final Object uniqueRef = model.getValue(KName.RULE_UNIQUE);
            if (uniqueRef instanceof String) {
                model.put(KName.RULE_UNIQUE, new JsonObject((String) uniqueRef));
            }
        }
        content.put(KName.MODEL, model);

        // 属性处理
        final JsonArray attributes = Ut.serializeJson(this.attributes.values());
        content.put(KName.Modeling.ATTRIBUTES, attributes);

        // Join专用
        final JsonArray joins = Ut.serializeJson(this.joins);
        content.put(KName.Modeling.JOINS, joins);

        // Schema 信息
        final JsonArray schemata = new JsonArray();
        this.schemata.stream().map(Schema::toJson).forEach(schemata::add);
        content.put(KName.Modeling.SCHEMATA, schemata);
        return content;
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        content.append("-- Model Begin ------------------------------");
        content.append("\nfile = ").append(this.jsonFile);
        content.append("\nidentifier = ").append(this.identifier);
        final String namespace = this.ark.app().ns();
        content.append("\nnamespace = ").append(namespace);
        content.append("\n\t").append(this.model.toString());
        content.append("\nattributes :");
        this.attributes.forEach((k, v) -> content.append("\n\t")
            .append(k).append(" = ").append(v.toString()));
        content.append("\nschemas : [");
        this.schemata.forEach(schema -> content.append("\n")
            .append(schema.toString()));
        content.append("\n]\njoins :");
        this.joins.forEach(item -> content.append("\n\t")
            .append(this.identifier).append("=")
            .append(item.getEntity()).append(",")
            .append(item.getEntityKey()));
        content.append("\n-- Model End --------------------------------\n");
        return content.toString();
    }
}
