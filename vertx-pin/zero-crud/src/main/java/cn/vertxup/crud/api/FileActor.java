package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.next.WJoin;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
                    Pre.fabric()::inAAsync      /* Dict */
                )
                .next(in -> WJoin.on(in)::runAAsync)
                .passion(Agonic.file(false)::runAAsync)
                .runA(data)
        );

    }

    @Address(Addr.File.EXPORT)
    public Future<Envelop> exportFile(final Envelop request) {
        /* Headers */
        final ConcurrentMap<String, String> exportedHeaders = new ConcurrentHashMap<>();
        /* Removed */
        final JsonArray removed = new JsonArray();
        /* Column sequence */
        final List<String> columnList = new ArrayList<>();
        /* Search full column and it will be used in another method */
        return null;
    }

}
