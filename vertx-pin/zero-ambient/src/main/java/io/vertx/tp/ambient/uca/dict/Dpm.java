package io.vertx.tp.ambient.uca.dict;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.eon.em.GlossaryType;

import java.util.concurrent.ConcurrentMap;

/**
 * ## The Dict Interface
 *
 * ### 1. Intro
 *
 * You can add different dict type in current plugin and modular here.
 *
 * - TABULAR, Related to `X_TABULAR` ( Global List )
 * - ASSIST, Any static source with jooq Dao.
 * - CATEGORY, Related to `X_CATEGORY` ( Global Tree )
 * - NONE, ( Default here for the definition that could not be parsed )
 *
 * ### 2. Meaning
 *
 * The DPM means `Data Processing Management`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Dpm {
    /**
     * Async source
     *
     * @param params {@link MultiMap} parameters that are formatted with vertx
     * @param source {@link DictSource} definition of dict here.
     *
     * @return {@link Future}
     */
    Future<ConcurrentMap<String, JsonArray>> fetchAsync(DictSource source, MultiMap params);

    /**
     * Sync source
     *
     * @param params {@link MultiMap} parameters that are formatted with vertx
     * @param source {@link DictSource} definition of dict here.
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<String, JsonArray> fetch(DictSource source, MultiMap params);

    static Dpm get(final GlossaryType type) {
        return DpmTool.POOL_DPM.getOrDefault(type, null);
    }
}
