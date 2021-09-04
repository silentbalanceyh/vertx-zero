package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.tp.ke.atom.KModule;
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
        final String filename = Ux.getString1(envelop);
        final String module = Ux.getString2(envelop);
        final JsonObject params = new JsonObject().put(KName.FILE_NAME, filename);

        final IxPanel panel = IxPanel.on(envelop, module);
        return Pre.excel(this.client).inJAAsync(params, panel.active()).compose(data ->
            IxPanel.on(envelop, module)
                .input(
                    Pre.fabric(true)::inAAsync      /* Dict */
                )
                .next(in -> (input, active) -> Ux.future(active))
                .passion(Agonic.file()::runAAsync)
                .runA(data)
        );

    }

    @Address(Addr.File.EXPORT)
    public Future<Envelop> exportFile(final Envelop envelop) {
        /* Search full column and it will be used in another method */
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, Ux.getString1(envelop));         // view
        final String module = Ux.getString2(envelop);           // module
        final JsonObject condition = Ux.getJson(envelop, 3);

        /* Exported columns here for future calculation */
        final JsonArray projection = Ut.sureJArray(condition.getJsonArray(KName.Ui.COLUMNS));
        final List<String> columnList = Ut.toList(projection);
        /* Remove columns here and set criteria as condition
         * Here extract query by `criteria` node, it will be synced with
         * dynamic exporting here.
         **/
        JsonObject criteria = Ut.sureJObject(condition.getJsonObject(Qr.KEY_CRITERIA));
        final IxPanel panel = IxPanel.on(envelop, module);
        return T.fetchFull(envelop, module).runJ(params)
            .compose(columns -> panel
                /*
                 * Data Processing
                 */
                .input(
                    Pre.codex()::inJAsync /* Rule Vrify */
                )
                .passion(Agonic.fetch()::runJAAsync, null)
                .<JsonArray, JsonObject, JsonArray>runJ(criteria)
                /* Dict Transfer to Export */
                .compose(data -> Pre.fabric(false).inAAsync(data, panel.active()))
                .compose(data -> Post.export(columnList).outAsync(data, columns))
                .compose(data -> {
                    /*
                     * Data Extraction for file buffer here
                     */
                    if (data instanceof JsonArray) {
                        final IxIn active = panel.active();
                        final KModule in = active.module();
                        /*
                         * The system will calculate the type definition of static module
                         */
                        final TypeAtom atom = Ix.onAtom(active, (JsonArray) columns);
                        return this.client.exportAsync(in.getIdentifier(), (JsonArray) data, atom);
                    } else {
                        return Ux.future(Buffer.buffer());
                    }
                })
            ).compose(buffer -> Ux.future(Envelop.success(buffer)));
    }
}
