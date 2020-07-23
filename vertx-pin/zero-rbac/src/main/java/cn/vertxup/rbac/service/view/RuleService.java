package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.em.SourceType;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RuleService implements RuleStub {
    @Override
    public Future<JsonArray> procAsync(final List<SPath> paths) {
        final List<SPath> filtered = paths.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return Ux.thenCombineT(filtered, this::procRule).compose(Ux::fnJArray);
    }

    private Future<JsonObject> procRule(final SPath path) {
        final JsonObject serialized = Ut.serializeJson(path);
        return Ke.mount("uiCondition", "groupCondition", "groupConfig").apply(serialized).compose(processed -> {
            final SourceType type = Ut.toEnum(path::getUiType, SourceType.class, SourceType.ERROR);
            if (SourceType.ERROR == type) {
                return Ux.future(processed);
            } else {
                return Ux.future(processed)
                        /* node `ui` processing */
                        .compose(this.toUi(type, path));
            }
        });
    }

    private Function<JsonObject, Future<JsonObject>> toUi(final SourceType type, final SPath path) {
        return processed -> {
            final RuleSource dao = RuleSource.EXECUTOR.get(type);
            if (Objects.isNull(dao)) {
                return Ux.future(processed);
            } else {
                /* */
                final JsonObject inputData = new JsonObject();
                inputData.put(KeField.SIGMA, path.getSigma());
                inputData.put(KeField.LANGUAGE, path.getLanguage());
                return dao.procAsync(inputData, processed).compose(ui -> {
                    /* ui node */
                    processed.put("ui", ui);
                    processed.remove("uiCondition");
                    processed.remove("uiComponent");
                    processed.remove("uiType");
                    return Ux.future(processed);
                });
            }
        };
    }
}
