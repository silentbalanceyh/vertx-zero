package io.modello.util;

import io.horizon.eon.VName;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.EmApp;
import io.horizon.runtime.Macrocosm;
import io.horizon.util.HUt;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.macrocosm.specification.secure.HoI;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Optional;

/**
 * @author lang : 2023-06-06
 */
class MContext {

    static String keyApp(final String name, final String ns, final String owner) {
        return keyOwner(owner) + VString.SLASH + ns + VString.SLASH + name;
    }

    static String keyApp(final HArk ark) {
        Objects.requireNonNull(ark);
        final String ownerId = HUt.keyOwner(Optional.ofNullable(ark.owner())
            .map(HoI::owner).orElse(VValue.DEFAULT));
        final HApp app = ark.app();
        Objects.requireNonNull(app);
        return ownerId + VString.SLASH + app.ns() + VString.SLASH + app.name();
    }

    static String keyOwner(final String id) {
        String valueOwner = HUt.envWith(Macrocosm.Z_TENANT, id);
        if (HUt.isNil(valueOwner)) {
            valueOwner = VValue.DEFAULT;
        }
        return valueOwner;
    }

    static JsonObject qrApp(final HArk ark, final EmApp.Mode mode) {
        final JsonObject condition = qrCube(ark);

        final HApp app = ark.app();
        if (Objects.nonNull(app)) {
            condition.put(VName.APP_ID, app.appId());
            condition.put(VName.APP_KEY, app.option(VName.APP_KEY));
        }

        if (EmApp.Mode.CUBE != mode) {
            final HoI belong = ark.owner();
            condition.put(VName.TENANT_ID, belong.owner());
        }
        return condition;
    }

    static JsonObject qrService(final HArk ark, final EmApp.Mode mode) {
        final JsonObject condition = qrCube(ark);
        final HApp app = ark.app();
        if (Objects.nonNull(app)) {
            condition.put(VName.NAME, app.name());
            condition.put(VName.NAMESPACE, app.ns());
        }
        if (EmApp.Mode.CUBE != mode) {
            final HoI belong = ark.owner();
            condition.put(VName.TENANT_ID, belong.owner());
        }
        return condition;
    }

    private static JsonObject qrCube(final HArk ark) {
        final JsonObject condition = new JsonObject();
        condition.put(VName.SIGMA, ark.sigma());
        condition.put(VName.LANGUAGE, ark.language());
        return condition;
    }
}
