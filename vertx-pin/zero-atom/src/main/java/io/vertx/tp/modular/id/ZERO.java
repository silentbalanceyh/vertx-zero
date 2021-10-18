package io.vertx.tp.modular.id;

import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.vertx.tp.atom.cv.em.IdMode;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.error._417PrimaryKeyResultException;
import io.vertx.tp.error._417PrimaryKeySizeException;
import io.vertx.up.fn.Fn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<IdMode, AoId> POOL_ID = new ConcurrentHashMap<IdMode, AoId>() {
        {
            this.put(IdMode.DIRECT, new DirectId());
            this.put(IdMode.COLLECTION, new CollectionId());
            this.put(IdMode.JOIN_KEY, new JoinKeyId());
            this.put(IdMode.JOIN_MULTI, new JoinMultiId());
            this.put(IdMode.JOIN_COLLECTION, new JoinCollectionId());
        }
    };
}

class
Ensurer {
    /*
     * Join 模式的定义
     */
    static Set<Boolean> join(final Class<?> clazz,
                             final Model model) {
        final Set<MJoin> joins = model.dbJoins();
        /*
         * 第一检查条件：一对多的 Join 模式，keyMap的尺寸必须大于 1
         */
        Fn.outWeb(1 >= joins.size(), _417PrimaryKeyResultException.class, clazz,
            /* ARG1：出现该异常的目标类名 */ clazz.getName(),
            /* ARG2：当前实体的主键信息 */ AoId.keyInfo(joins));

        /*
         * 第二检查条件：keyMap的尺寸 和 schema 的数量一致
         * Join Multi 必须是：
         * key -> Entity1 / key
         * key -> Entity2 / ciKey / key
         */
        Fn.outWeb(joins.size() < model.schemata().size(), _417PrimaryKeySizeException.class, clazz,
            /* ARG1：实际的主键数量 */ model.schemata().size(), // 没个 Schema 一个主键
            /* ARG2：期望的主键数量 */ String.valueOf(joins.size()));

        final Set<Boolean> valid = new HashSet<>();
        joins.forEach((entry) -> {
            final String identifier = entry.getEntity();
            final String keyField = entry.getEntityKey();

            final Schema schema = model.schema(identifier);
            final List<MField> keys = schema.getPrimaryKeys();

            /*
             * 第三检查条件：没个 Schema 的主键只能是单字段主键
             */
            Fn.outWeb(1 < keys.size(), _417PrimaryKeySizeException.class, clazz,
                /* ARG1：实际的主键数量 */ keys.size(),
                /* ARG2：期望的主键数量 */ String.valueOf(1));

            final MField field = schema.getField(keyField);
            valid.add(field.getIsPrimary());
        });
        /* 返回的主键的数量 */
        return valid;
    }
}