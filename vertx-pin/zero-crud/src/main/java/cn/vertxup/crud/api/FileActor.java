package cn.vertxup.crud.api;

import io.aeon.experiment.specification.KModule;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.syntax.Ir;
import io.modello.specification.meta.HMetaAtom;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.mod.crud.cv.em.ApiSpec;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.desk.IxPanel;
import io.vertx.mod.crud.uca.desk.IxWeb;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.next.Co;
import io.vertx.mod.crud.uca.op.Agonic;
import io.vertx.mod.crud.uca.trans.Tran;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.plugin.excel.ExcelClient;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

@Queue
@SuppressWarnings("all")
public class FileActor {

    private static final Annal LOGGER = Annal.get(FileActor.class);

    @Infusion
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
                Tran.initial()::inAAsync,                   /* Initial */
                Tran.fabric(true)::inAAsync,          /* Dict */
                Tran.map(true)::inAAsync,             /* Mapping */
                Tran.tree(true)::inAAsync             /* Tree Transform in Importing */
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
        final JsonArray projection = Ut.valueJArray(condition.getJsonArray(KName.Ui.COLUMNS));
        final List<String> columnList = Ut.toList(projection);
        /* Remove columns here and set criteria as condition
         * Here extract query by `criteria` node, it will be synced with
         * dynamic exporting here.
         **/
        JsonObject criteria = Ut.valueJObject(condition.getJsonObject(Ir.KEY_CRITERIA));
        final IxPanel panel = IxPanel.on(request);
        final IxMod mod = request.active();
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
                .compose(data -> Tran.map(false).inAAsync(data, mod))       /* Map */
                .compose(data -> Tran.fabric(false).inAAsync(data, mod))    /* Dict */
                .compose(data -> Tran.tree(false).inAAsync(data, mod))      /* Tree */
                .compose(data -> Pre.audit().inAAsync(data, mod))           /* Auditor */
                .compose(data -> Co.endE(columnList).ok(data, columns))
                .compose(data -> {
                    /*
                     * Data Extraction for file buffer here
                     */
                    if (data instanceof JsonArray) {
                        final IxMod active = mod;
                        final KModule in = active.module();
                        /*
                         * The system will calculate the type definition of static module
                         */
                        final HMetaAtom atom = Ix.onAtom(active, (JsonArray) columns);
                        return this.client.exportAsync(in.getTable(), (JsonArray) data, atom);
                    } else {
                        return Ux.future(Buffer.buffer());
                    }
                })
            ).compose(buffer -> Ux.future(Envelop.success(buffer)));
    }
}
