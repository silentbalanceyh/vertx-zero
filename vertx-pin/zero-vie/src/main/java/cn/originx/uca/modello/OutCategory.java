package cn.originx.uca.modello;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutCategory extends OutDpmExpr implements OComponent {
    ConcurrentMap<String, ConcurrentHashMap<String, JsonObject>> index = new ConcurrentHashMap<>();

    @Override
    public Object after(Kv<String, Object> kv, Record record, JsonObject combineData) {
        Object translated = this.translateTo(kv.getValue(), combineData);
        Object expressed = this.express(Kv.create(kv.getKey(), translated), record, combineData);
        return this.normalize(Kv.create(kv.getKey(), expressed), record, combineData);
    }

    /**
     * 1. 依据分类key找寻identifier级别链，如ci.network.vpn -> ci.network
     * 2. 根据identifier级别链找寻适当的sourceNorm映射
     * 3. 返回映射结果
     */
    Object normalize(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        String cat3Key, cat2Key, cat3Identifier, cat2Identifier;
        JsonObject cat3Record, cat2Record;
        String normalized;
        Object value0 = kv.getValue(); //category key
        if (Objects.nonNull(value0)) {
            cat3Key = (String) value0;
            JsonObject sourceNorm = Ut.sureJObject(combineData.getJsonObject(KName.SOURCE_NORM));
            JsonArray records2Index = Ut.visitJArray(combineData, KName.SOURCE_DATA, "ci.type");
            this.createIndexes(records2Index);

            // 找寻identifier链
            cat3Record = this.index.get(KName.KEY).get(cat3Key);
            cat3Identifier = cat3Record.getString(KName.IDENTIFIER);
            cat2Key = cat3Record.getString("parentId");
            cat2Record = this.index.get(KName.KEY).get(cat2Key);
            cat2Identifier = cat2Record.getString(KName.IDENTIFIER);

            //找寻映射
            if (Ut.notNil(sourceNorm)) {
                normalized = sourceNorm.getString(cat3Identifier);
                if (Ut.notNil(normalized)) {
                    return normalized;
                } else {
                    normalized = sourceNorm.getString(cat2Identifier);
                    if (Ut.notNil(normalized)) {
                        return normalized;
                    }
                }
            }
        }
        return value0;
    }

    void createIndexes(JsonArray records2Index) {
        this.createIndexKey(records2Index);
    }

    void createIndex(JsonArray records2Index, String field) {
        JsonObject category;
        if (Objects.isNull(this.index.get(field))) {
            for (Object o : records2Index) {
                category = (JsonObject) o;
                this.index.get(field).putIfAbsent(category.getString(field), category);
            }
        }
    }

    void createIndexKey(JsonArray records2Index) {
        this.createIndex(records2Index, KName.KEY);
    }
}
