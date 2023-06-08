package io.horizon.spi.extension;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ambient.cv.AtMsg;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.excel.ExcelClient;
import io.vertx.up.plugin.excel.ExcelInfix;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vertx.mod.ambient.refine.At.LOG;

public class DatumInit implements Init {

    private static final Annal LOGGER = Annal.get(DatumInit.class);
    private static final AtConfig CONFIG = AtPin.getConfig();

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return appJson -> {
            LOG.App.info(LOGGER, AtMsg.INIT_DATUM, appJson.encode());
            return this.doLoading(appJson)
                /* Extension */
                .compose(this::doExtension);
        };
    }

    public Future<JsonObject> doExtension(final JsonObject appJson) {
        final Init loader = AtPin.getLoader();
        if (Objects.isNull(loader)) {
            return Ux.future(appJson);
        } else {
            return loader.apply().apply(appJson);
        }
    }

    private Future<JsonObject> doLoading(final JsonObject appJson) {
        /* Datum Loading */
        final String dataFolder = CONFIG.getDataFolder();
        final List<String> files = Ut.ioFiles(dataFolder);
        /* List<Future> */
        final List<Future<JsonObject>> futures = files.stream()
            .filter(Ut::isNotNil)
            /* Remove temp file of Excel */
            .filter(file -> !file.startsWith("~$"))
            .map(file -> dataFolder + file)
            .map(this::doLoading)
            .collect(Collectors.toList());
        return Fn.combineA(futures)
            /* Stored each result */
            .compose(results -> UObject.create().append(KName.RESULT, results)
                .toFuture())
            .compose(results -> Ux.future(this.result(results, appJson)));
    }

    private Future<JsonObject> doLoading(final String filename) {
        return Ux.nativeWorker(filename, pre -> {
            /* ExcelClient */
            final ExcelClient client = ExcelInfix.createClient();
            client.importAsync(filename, result -> {
                LOG.App.info(LOGGER, AtMsg.INIT_DATUM_EACH, filename);
                if (result.succeeded()) {
                    pre.complete(Ut.endBool(Boolean.TRUE, filename));
                } else {
                    pre.fail(result.cause());
                }
            });
        });
    }

    @Override
    public JsonObject result(final JsonObject input, final JsonObject appJson) {
        /* Extract Failure Filename, No thing to do or */
        return appJson;
    }
}
