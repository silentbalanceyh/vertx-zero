package io.vertx.up.uca.jooq.util;

import io.horizon.eon.VString;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/25
 */
public class JqCond {
    /*
     * For Importing Transfer
     * {
     *     "": false,
     *     "$1": {
     *         "": true
     *     },
     *     "$2": {
     *         "": true
     *     }
     * }
     */
    public static JsonObject compress(final JsonObject queryOr) {
        if (queryOr.containsKey(Ir.KEY_CRITERIA)) {
            queryOr.put(Ir.KEY_CRITERIA, compress(queryOr.getJsonObject(Ir.KEY_CRITERIA)));
            return queryOr;
        } else {
            final Boolean isAnd = queryOr.getBoolean(VString.EMPTY, Boolean.FALSE);
            if (isAnd) {
                // 顶层AND跳过
                return queryOr;
            }

            // 每棵树执行公共部分提取
            /*
             * 双节点计算
                "$66" : {
                  "rltPricecat" : "5fd56c77-ead4-4899-aa8b-f54ec622588b",
                  "rRoomTypeId" : "96bc9581-3ed7-440b-859c-fbd5c1cd77df",
                  "zSigma" : "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                  "" : true
                },
                "$67" : {
                  "rltPricecat" : "936052be-7d46-41e8-a0dc-69b2db2fbe30",
                  "rRoomTypeId" : "96bc9581-3ed7-440b-859c-fbd5c1cd77df",
                  "zSigma" : "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                  "" : true
                }
             * 结果
                {
                  "rltPricecat,i": [],
                  "rRoomTypeId" : "96bc9581-3ed7-440b-859c-fbd5c1cd77df",
                  "zSigma" : "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                  "" : true
                }
             */
            final ConcurrentMap<String, Set<String>> tree = new ConcurrentHashMap<>();
            for (final String field : queryOr.fieldNames()) {
                final Object conditionJ = queryOr.getValue(field);
                if (conditionJ instanceof final JsonObject json) {

                }
            }
        }
        return queryOr;
    }
}
