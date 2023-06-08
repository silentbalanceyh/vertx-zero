package io.vertx.mod.atom.modeling.builtin;

import cn.vertxup.atom.domain.tables.pojos.MEntity;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MIndex;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.modello.dynamic.modular.apply.AoDefault;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.jooq.tools.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class DataSchema implements Schema {

    private final transient ConcurrentMap<String, MKey> keys =
        new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, MField> fields =
        new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, MIndex> indexes =
        new ConcurrentHashMap<>();

    private final transient String namespace;
    private transient String identifier;
    private transient MEntity entity;
    /* Json文件信息 */
    private transient String jsonFile;

    public DataSchema(final String namespace) {
        this.namespace = namespace;
    }

    /**
     * 读取当前Schema的所有字段名
     */
    @Override
    public Set<String> getFieldNames() {
        return this.fields.values().stream()
            .map(MField::getName)
            .collect(Collectors.toSet());
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public String getTable() {
        return this.entity.getTableName();
    }

    @Override
    public MField getFieldByColumn(final String column) {
        return this.fields.values().stream()
            .filter(field -> StringUtils.equals(field.getColumnName(), column))
            .findFirst().orElse(null);
    }

    @Override
    public MField getField(final String field) {
        return this.fields.get(field);
    }

    /**
     * 读取所有列名
     */
    @Override
    public Set<String> getColumnNames() {
        return this.fields.values().stream()
            .map(MField::getColumnName)
            .collect(Collectors.toSet());
    }

    /**
     * 读取当前Schema中所有主键
     */
    @Override
    public List<MField> getPrimaryKeys() {
        return this.fields.values().stream()
            .filter(MField::getIsPrimary)
            .collect(Collectors.toList());
    }

    /**
     * 读取当前Schema关联实体
     */
    @Override
    public MEntity getEntity() {
        return this.entity;
    }

    /**
     * 读取字段集合
     */
    @Override
    public MField[] getFields() {
        return this.fields.values().toArray(new MField[]{});
    }

    /**
     * 读取键集合
     */
    @Override
    public MKey[] getKeys() {
        return this.keys.values().toArray(new MKey[]{});
    }

    @Override
    public String resource() {
        return this.jsonFile;
    }

    @Override
    public void connect(final String key) {
        // 设置 MEntity 主键
        this.entity.setKey(key);
        // 修改 MKey 关联主键
        this.keys.values().forEach(keyItem -> keyItem.setEntityId(key));
        // 修改 MField 关联主键
        this.fields.values().forEach(fieldItem -> fieldItem.setEntityId(key));
    }

    @Override
    public JsonObject toJson() {
        final JsonObject content = new JsonObject();

        // 实体处理
        final JsonObject entity = Ut.serializeJson(this.entity);
        content.put(KName.ENTITY, entity);

        // 键处理
        final JsonArray keys = Ut.serializeJson(this.keys.values());
        content.put(KName.Modeling.KEYS, keys);

        // 字段处理
        final JsonArray fields = Ut.serializeJson(this.fields.values());
        content.put(KName.Modeling.FIELDS, fields);
        return content;
    }

    @Override
    public void fromFile(final String file) {
        // Model会关心文件路径，所以这里需要这个操作
        this.jsonFile = file;
        final JsonObject data = Ut.ioJObject(this.jsonFile);
        this.fromJson(data);
    }

    @Override
    public void fromJson(final JsonObject json) {
        // 第二次
        this.keys.clear();
        this.fields.clear();

        // 直接处理 json 数据，拷贝防止副作用
        final JsonObject data = null == json ? new JsonObject() : json.copy();
        // Schema 默认值处理，替换原始的OxJDefault
        AoDefault.schema().applyJson(data);

        // 直接从JsonObject中读取数据
        JsonObject entityJson = data.getJsonObject(KName.ENTITY);
        entityJson = null == entityJson ? new JsonObject() : entityJson.copy();
        AoDefault.entity().applyJson(entityJson);
        // 反序列化实体
        {
            this.entity = Ut.deserialize(entityJson, MEntity.class);
            // 设置名空间
            this.entity.setNamespace(this.namespace);
            this.identifier = this.entity.getIdentifier();
        }
        // 填充Key
        final JsonArray keys = data.getJsonArray(KName.Modeling.KEYS);
        Ut.itJArray(keys, (key, index) -> {
            // 设置key的默认值
            AoDefault.key().mount(this.entity).applyJson(key);
            final MKey keyObj = Ut.deserialize(key, MKey.class);
            if (Ut.isNotNil(keyObj.getName())) {
                this.keys.put(keyObj.getName(), keyObj);
            }
        });

        // 填充Field
        final JsonArray fields = data.getJsonArray(KName.Modeling.FIELDS);
        Ut.itJArray(fields, (field, index) -> {
            // 设置field的默认值
            AoDefault.field().mount(this.entity).applyJson(field);
            final MField fieldObj = Ut.deserialize(field, MField.class);
            if (Ut.isNotNil(fieldObj.getName())) {
                this.fields.put(fieldObj.getName(), fieldObj);
            }
        });
    }

    @Override
    public String toString() {
        final StringBuilder content = new StringBuilder();
        content.append("-- Schema Begin ------------------------------");
        content.append("\nfile = ").append(this.jsonFile);
        content.append("\nidentifier = ").append(this.identifier);
        content.append("\nnamespace = ").append(this.namespace);
        content.append("\n\t").append(this.entity.toString());
        content.append("\nkeys :");
        this.keys.forEach((k, v) -> content.append("\n\t")
            .append(k).append(" = ").append(v.toString())
        );
        content.append("\nfields :");
        this.fields.forEach((k, v) -> content.append("\n\t")
            .append(k).append(" = ").append(v.toString())
        );
        content.append("\n-- Schema End --------------------------------\n");
        return content.toString();
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataSchema)) {
            return false;
        }
        final DataSchema that = (DataSchema) o;
        return Objects.equals(this.identifier, that.identifier) &&
            Objects.equals(this.entity.getNamespace(), that.entity.getNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier, this.entity.getNamespace());
    }
}
