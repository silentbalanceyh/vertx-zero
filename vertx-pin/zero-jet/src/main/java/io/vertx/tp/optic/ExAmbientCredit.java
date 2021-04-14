package io.vertx.tp.optic;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.ke.atom.KCredential;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.optic.environment.Ambient;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class ExAmbientCredit implements Credential {
    @Override
    public Future<KCredential> fetchAsync(final String sigma) {
        final JtApp app = Ambient.getApp(sigma);
        final KCredential idc = new KCredential();
        if (Objects.nonNull(app)) {
            final JsonObject credential = new JsonObject();
            credential.put(KeField.SIGMA, sigma);
            credential.put(KeField.APP_ID, app.getAppId());
            credential.put(KeField.REALM, app.getName());
            credential.put(KeField.GRANT_TYPE, "authorization_code");
            idc.fromJson(credential);
        }
        return Ux.future(idc);
    }
}
