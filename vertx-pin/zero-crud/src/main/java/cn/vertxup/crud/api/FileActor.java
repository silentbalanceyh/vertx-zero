package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.em.ApiSpec;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.desk.IxWeb;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.tran.Co;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

@Queue
@SuppressWarnings("all")
public class FileActor {

    private static final Annal LOGGER = Annal.get(FileActor.class);

    @Plugin
    private transient ExcelClient client;

    @Address(Addr.File.IMPORT)
    public Future<Envelop> importFile(final Envelop envelop) {
        /*
         *  Import Data from file here
         *  Extract `filename` as file
         */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_STRING).build(envelop);
        final IxPanel panel = IxPanel.on(request);
        final Co co = Co.nextJ(request.active(), true);
        return Pre.excel(this.client).inJAAsync(request.dataF(), request.active()).compose(data -> panel
            .input(
                Pre.initial()::inAAsync,         /* Initial */
                Pre.fabric(true)::inAAsync       /* Dict */
            )
            .next(in -> co::next)
            .output(co::ok)
            .passion(Agonic.file()::runAAsync)
            .runA(data)
        );

    }

    @Address(Addr.File.EXPORT)
    public Future<Envelop> exportFile(final Envelop envelop) {
        /* Search full column and it will be used in another method */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);

        /* Exported columns here for future calculation */
        final JsonObject condition = request.dataJ();
        final JsonArray projection = Ut.sureJArray(condition.getJsonArray(KName.Ui.COLUMNS));
        final List<String> columnList = Ut.toList(projection);
        /* Remove columns here and set criteria as condition
         * Here extract query by `criteria` node, it will be synced with
         * dynamic exporting here.
         **/
        JsonObject criteria = Ut.sureJObject(condition.getJsonObject(Qr.KEY_CRITERIA));
        final IxPanel panel = IxPanel.on(request);
        return T.fetchFull(request).runJ(request.dataV())
            /*
             * Data Processing
             */
            .compose(columns -> panel
                .input(
                    Pre.codex()::inJAsync /* Rule Vrify */
                )
                .passion(Agonic.fetch()::runJAAsync, null)
                .<JsonArray, JsonObject, JsonArray>runJ(criteria)
                /* Dict Transfer to Export */
                .compose(data -> Pre.fabric(false).inAAsync(data, request.active()))    /* Dict */
                .compose(data -> Pre.tree(false).inAAsync(data, request.active()))      /* Tree */
                .compose(data -> Co.endE(columnList).ok(data, columns))
                .compose(data -> {
                    /*
                     * Data Extraction for file buffer here
                     */
                    if (data instanceof JsonArray) {
                        final IxMod active = request.active();
                        final KModule in = active.module();
                        /*
                         * The system will calculate the type definition of static module
                         */
                        final TypeAtom atom = Ix.onAtom(active, (JsonArray) columns);
                        return this.client.exportAsync(in.getTable(), (JsonArray) data, atom);
                    } else {
                        return Ux.future(Buffer.buffer());
                    }
                })
            ).compose(buffer -> Ux.future(Envelop.success(buffer)));
    }
}
