package io.vertx.mod.ambient.aop;

import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.hoc.checker.HocTrue;
import io.horizon.specification.uca.HTrue;
import io.horizon.uca.aop.Before;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.uca.validator.TreeChecker;
import io.vertx.up.fn.Fn;

import java.util.Set;

/**
 * @author lang : 2023-05-27
 */
public class BeforeTreeChecker implements Before {
    private final HTrue<XCategory> checker = TreeChecker.of();

    @Override
    public Set<ChangeFlag> types() {
        return Set.of(ChangeFlag.DELETE);
    }

    @Override
    public Future<JsonObject> beforeAsync(final JsonObject data, final JsonObject config) {
        // 全为 false 过
        return Fn.passNone(data, HocTrue.web403Link(this.getClass(), data), Set.of(
            (input) -> this.checker.executeJAsync(data, config)
        ));
    }

    @Override
    public Future<JsonArray> beforeAsync(final JsonArray data, final JsonObject config) {
        // 全为 false 过
        return Fn.passNone(data, HocTrue.web403Link(this.getClass(), data), Set.of(
            (input) -> this.checker.executeJAsync(data, config)
        ));
    }
}
