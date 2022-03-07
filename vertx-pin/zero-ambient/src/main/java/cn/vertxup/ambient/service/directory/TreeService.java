package cn.vertxup.ambient.service.directory;

import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.feature.Arbor;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TreeService implements TreeStub {
    @Inject
    private transient DatumStub stub;

    @Override
    public Future<JsonArray> seekAsync(final String appId, final String type) {
        return this.stub.treeApp(appId, type).compose(categories -> {
            final List<Future<JsonObject>> futures = new ArrayList<>();
            Ut.itJArray(categories).map(this::seekAsync).forEach(futures::add);
            return Ux.thenCombine(futures);
        });
    }

    private Future<JsonObject> seekAsync(final JsonObject input) {
        final String runComponent = input.getString(KName.Component.TREE_COMPONENT);
        final Class<?> arborCls = Ut.clazz(runComponent, null);
        if (Objects.isNull(arborCls) || !Ut.isImplement(arborCls, Arbor.class)) {
            // Nothing To Do
            return Ux.future(input);
        }
        final JsonObject configuration = Ut.toJObject(input.getString(KName.Component.TREE_CONFIG));
        final Arbor arbor = Ut.instance(arborCls);
        return arbor.generate(input, configuration).compose(children -> {
            /*
             * Configuration Extract Mapping
             * {
             *      "field1": "treeField1",
             *      "field2": "treeField2",
             *      "field3": "treeField3",
             *      "field4": "treeField4",
             * }
             */
            final JsonObject mapping = configuration.getJsonObject(KName.MAPPING, new JsonObject());
            mapping.fieldNames().forEach(from -> {
                final String to = mapping.getString(from);
                Ut.itJArray(children).forEach(child -> child.put(to, child.getValue(from)));
            });
            input.put(KName.CHILDREN, children);
            return Ux.future(input);
        });
    }
}
