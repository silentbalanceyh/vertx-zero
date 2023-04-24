package cn.originx.migration.restore;

import cn.originx.migration.AbstractStep;
import cn.originx.migration.modeling.Revision;
import cn.originx.refine.Ox;
import cn.vertxup.atom.domain.tables.daos.MAttributeDao;
import cn.vertxup.atom.domain.tables.daos.MEntityDao;
import cn.vertxup.atom.domain.tables.daos.MFieldDao;
import cn.vertxup.atom.domain.tables.daos.MModelDao;
import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MEntity;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/*
 * 建模限制文件的读取导入，运行在 OxFinisher 之后
 * 变更已导入模型的 metadata 相关信息
 * {
 *     "edition": false,
 *     "deletion": false
 * }
 * 是否可删除 / 是否可编辑的设置
 */
public class MetaLimit extends AbstractStep {

    public MetaLimit(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002.3-2. 修正建模数据");
        /*
         * 读取当前应用中所有模型
         */
        return Ux.future(config)
            /* MEntity 修正 */
            .compose(this::procEntity)
            /* MField 修正 */
            .compose(this::procFields)
            /* MModel 修正 */
            .compose(this::procModel)
            /* MAttribute 修正 */
            .compose(this::procAttributes)
            .compose(adjust -> {
                Ox.Log.infoShell(this.getClass(), "数据修正成功完成！Successfully");
                return Ux.future(config);
            });
    }

    private Future<JsonObject> procEntity(final JsonObject config) {
        this.banner("002.3-2-1. Entity 修正");
        return this.<MEntity>procUniform(MEntityDao.class,
            entities -> Ut.elementMap(entities, MEntity::getKey, MEntity::getIdentifier));
    }

    private Future<JsonObject> procFields(final JsonObject config) {
        this.banner("002.3-2-2. Field 修正");
        return this.<MField>procUniform(MFieldDao.class,
            fields -> Ut.elementMap(fields, MField::getKey, MField::getName));
    }

    private Future<JsonObject> procAttributes(final JsonObject config) {
        this.banner("002.3-2-4. Attributes 修正");
        return this.<MAttribute>procUniform(MAttributeDao.class,
            fields -> Ut.elementMap(fields, MAttribute::getKey, MAttribute::getName));
    }

    private Future<JsonObject> procModel(final JsonObject config) {
        this.banner("002.3-2-3. Model 修正");
        return this.<MModel>procUniform(MModelDao.class,
            models -> Ut.elementMap(models, MModel::getKey, MModel::getIdentifier));
    }

    private <T> Future<JsonObject> procUniform(final Class<?> daoCls,
                                               final Function<List<T>, ConcurrentMap<String, String>> keyFn) {
        return Ux.Jooq.on(daoCls).<T>fetchAsync(KName.SIGMA, this.app.getSigma())
            .compose(entities -> {
                final ConcurrentMap<String, String> keyMap = keyFn.apply(entities);
                final Revision revision = Revision.get(daoCls);
                return revision.captureAsync(keyMap).compose(resultMap -> {
                    /*
                     * entities 每个元素的处理
                     */
                    entities.forEach(entity -> {
                        final String key = Ut.field(entity, KName.KEY);
                        JsonObject data = resultMap.get(key);
                        if (Objects.isNull(data)) {
                            data = new JsonObject();
                        }
                        /*
                         * metadata processing
                         */
                        final String metadata = data.encode();
                        Ut.field(entity, KName.METADATA, metadata);
                    });
                    return Ux.Jooq.on(daoCls).updateAsync(entities)
                        .compose(nil -> Ux.future(new JsonObject()));
                });
            });
    }
}
