package io.vertx.tp.optic.modeling;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.AttributeType;
import io.vertx.tp.atom.cv.em.ModelType;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.config.AoService;
import io.vertx.tp.atom.modeling.element.DataKey;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.modular.apply.AoDefault;
import io.vertx.up.commune.rule.RuleUnique;
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
public class JsonModel implements Model {
    private final transient Set<Schema> schemata
            = new HashSet<>();
    /* 所有关联的Entity的ID */
    private final transient Set<MJoin> joins
            = new HashSet<>();
    /* 延迟填充 */
    private final transient ConcurrentMap<String, Class<?>> attributeTypes
            = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, MAttribute> attributes
            = new ConcurrentHashMap<>();

    private final transient String namespace;
    /* 当前Model关联的模型 */
    private transient MModel model;
    /* 当前Model的主键信息 */
    private transient DataKey key;
    /* 唯一ID */
    private transient String identifier;
    /* Json文件信息 */
    private transient String jsonFile;
    /* Unique专用 */
    private transient RuleUnique unique;

    public JsonModel(final String namespace) {
        this.namespace = namespace;
    }

    @Override
    public MModel getModel() {
        return this.model;
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public Schema getSchema(final String identifier) {
        return Ut.isNil(identifier) ? null :
                this.schemata.stream()
                        .filter(schema -> identifier.equals(schema.identifier()))
                        .findFirst().orElse(null);
    }

    @Override
    public ConcurrentMap<String, Class<?>> types() {
        if (this.attributeTypes.isEmpty()) {
            /*
             * 读取所有 MAttribute
             */
            this.getAttributes().forEach(attribute -> {
                /*
                 * 属性构造
                 */
                final AttributeType type = Ut.toEnum(attribute::getType, AttributeType.class, AttributeType.INTERNAL);
                if (AttributeType.INTERNAL == type) {
                    /*
                     * type = INTERNAL 属性
                     */
                    final Schema schema = this.getSchema(attribute.getSource());
                    final MField field = schema.getField(attribute.getSourceField());
                    if (Objects.nonNull(field)) {
                        /*
                         * 属性 Map 处理
                         */
                        this.attributeTypes.put(attribute.getName(), Ut.clazz(field.getType()));
                    }
                } else if (AttributeType.REFERENCE == type) {
                    /*
                     * REFERENCE 新增类型，用于引用不同类型，主要应用于
                     * 1）读取
                     * 2）查询
                     *
                     * type = REFERENCE 属性
                     * 只支持两种数据类型
                     * JsonObject / JsonArray
                     *
                     * 这种类型禁止使用在写操作中：添加、删除、修改
                     */
                    final AoService service = new AoService(attribute);
                    this.attributeTypes.put(attribute.getName(), service.type());
                }
            });

        }
        return this.attributeTypes;
    }

    @Override
    public String file() {
        return this.jsonFile;
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public Set<Schema> getSchemata() {
        return this.schemata;
    }

    @Override
    public DataKey getKey() {
        return this.key;
    }

    @Override
    public void setKey(final DataKey key) {
        this.key = key;
    }

    @Override
    public ModelType getType() {
        final String typeStr = this.model.getType();
        final ModelType type = Ut.toEnum(ModelType.class, typeStr);
        return null == type ? ModelType.DIRECT : type;
    }

    @Override
    public Set<MAttribute> getAttributes() {
        return new HashSet<>(this.attributes.values());
    }

    @Override
    public MAttribute getAttribute(final String attributeName) {
        return this.attributes.get(attributeName);
    }

    @Override
    public Model onJson(final Set<Schema> schemas) {
        // 从Json文件中读取时：需要检查joins
        if (!this.joins.isEmpty()) {
            // 桥接
            Bridge.connect(this, schemas,
                    (found) -> Bridge.connect(this, found));
        }
        return this;
    }

    @Override
    public Set<MJoin> getJoins() {
        return this.joins;
    }

    @Override
    public void setRelation(final String key) {
        // 修改 MModel 主键
        this.model.setKey(key);
        // 修改 MAttribute 关联主键
        this.attributes.values().forEach(attribute -> attribute.setModelId(key));
    }

    @Override
    public void onDatabase(final Set<Schema> schemas) {
        // 从数据库中读取时：schemas 和 foundSchemas 相等
        Bridge.connect(this, schemas);
    }

    @Override
    public void fromFile(final String file) {
        // Model会关心文件路径，所以这里需要这个操作
        this.jsonFile = file;
        final JsonObject data = Ut.ioJObject(this.jsonFile);
        this.fromJson(data);
    }

    @Override
    public RuleUnique getUnique() {
        if (Objects.isNull(this.unique)) {
            final String content = this.model.getRuleUnique();
            if (Ut.notNil(content)) {
                this.unique = Ut.deserialize(content, RuleUnique.class);
            }
        }
        return this.unique;
    }

    @Override
    public void fromJson(final JsonObject json) {
        // 第二次
        this.attributes.clear();
        this.joins.clear();
        this.schemata.clear();
        // 直接从JsonObject中读取数据
        final JsonObject model = json.getJsonObject(KeField.MODEL);
        AoDefault.model().applyJson(model);
        // 针对 ruleUnique
        {
            final Object uniqueRef = model.getValue(KeField.RULE_UNIQUE);
            if (uniqueRef instanceof JsonObject) {
                /*
                 * 反序列化成 RuleUnique
                 */
                final JsonObject content = (JsonObject) uniqueRef;
                this.unique = Ut.deserialize(content, RuleUnique.class);
                model.put(KeField.RULE_UNIQUE, content.encode());
            }
        }
        {
            this.model = Ut.deserialize(model, MModel.class);
            // 直接设置名空间
            this.model.setNamespace(this.namespace);
            this.identifier = this.model.getIdentifier();
        }

        // 填充属性
        final JsonArray attributes = json.getJsonArray(KeField.Modeling.ATTRIBUTES);
        Ut.itJArray(attributes, (attribute, index) -> {
            // 设置attribute的默认值
            AoDefault.attribute().mount(this.model).applyJson(attribute);
            final MAttribute attributeObj = Ut.deserialize(attribute, MAttribute.class);
            if (Ut.notNil(attributeObj.getName())) {
                this.attributes.put(attributeObj.getName(), attributeObj);
            }
        });
        // 读取join，并且通过 join 计算关系
        final JsonArray joins = json.getJsonArray(KeField.Modeling.JOINS);
        Ut.itJArray(joins, (join, index) -> {
            // 设置join的值
            AoDefault.join().mount(this.model).applyJson(join);
            {
                final MJoin joinObj = Ut.deserialize(join, MJoin.class);
                this.joins.add(joinObj);
            }
        });
        // 读取schemata
        final JsonArray schemata = json.getJsonArray(KeField.Modeling.SCHEMATA);
        if (null != schemata) {
            /* 在填充 Schema 的过程中直接处理 DataKey */
            Ut.itJArray(schemata, (schema, index) -> {
                final Schema schemaObj = new JsonSchema(this.namespace);
                schemaObj.fromJson(schema);
                this.schemata.add(schemaObj);
            });
            Bridge.connect(this, this.namespace + "-" + this.identifier);
        }
    }

    @Override
    public JsonObject toJson() {
        final JsonObject content = new JsonObject();

        // 模型处理
        final JsonObject model = Ut.serializeJson(this.model);
        // 针对Unique
        {
            final Object uniqueRef = model.getValue(KeField.RULE_UNIQUE);
            if (uniqueRef instanceof String) {
                model.put(KeField.RULE_UNIQUE, new JsonObject((String) uniqueRef));
            }
        }
        content.put(KeField.MODEL, model);

        // 属性处理
        final JsonArray attributes = Ut.serializeJson(this.attributes.values());
        content.put(KeField.Modeling.ATTRIBUTES, attributes);

        // Join专用
        final JsonArray joins = Ut.serializeJson(this.joins);
        content.put(KeField.Modeling.JOINS, joins);

        // Schema 信息
        final JsonArray schemata = new JsonArray();
        this.schemata.stream().map(Schema::toJson).forEach(schemata::add);
        content.put(KeField.Modeling.SCHEMATA, schemata);
        return content;
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        content.append("-- Model Begin ------------------------------");
        content.append("\nfile = ").append(this.jsonFile);
        content.append("\nidentifier = ").append(this.identifier);
        content.append("\nnamespace = ").append(this.namespace);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonModel)) {
            return false;
        }
        final JsonModel that = (JsonModel) o;
        return Objects.equals(this.identifier, that.identifier) &&
                Objects.equals(this.model.getNamespace(), that.model.getNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.model.getNamespace(), this.identifier);
    }
}
