package io.vertx.mod.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.horizon.spi.extension.*;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.List;

/*
 * Tool class available in current service only
 */
public class At {
    private static final Cc<String, Init> CC_INIT = Cc.open();

    public static Init initApp() {
        return CC_INIT.pick(AppInit::new, AppInit.class.getName());
        // return Fn.po?l(Pool.INIT_POOL, AppInit.class.getName(), AppInit::new);
    }

    public static Init initSource() {
        return CC_INIT.pick(SourceInit::new, SourceInit.class.getName());
        // return Fn.po?l(Pool.INIT_POOL, SourceInit.class.getName(), SourceInit::new);
    }

    public static Init initDatabase() {
        return CC_INIT.pick(DatabaseInit::new, DatabaseInit.class.getName());
        // return Fn.po?l(Pool.INIT_POOL, DatabaseInit.class.getName(), DatabaseInit::new);
    }

    public static Init initData() {
        return CC_INIT.pick(DatumInit::new, DatumInit.class.getName());
        // return Fn.po?l(Pool.INIT_POOL, DatumInit.class.getName(), DatumInit::new);
    }

    public static List<String> generate(final XNumber number, final Integer count) {
        return AtSerial.generate(number, count);
    }

    public static Future<List<String>> generateAsync(final XNumber number, final Integer count) {
        return Ux.future(AtSerial.generate(number, count));
    }

    public static XNumber serialAdjust(final XNumber number, final Integer count) {
        return AtSerial.adjust(number, count);
    }

    public static Future<Buffer> fileDownload(final JsonArray attachment) {
        return AtFs.fileDownload(attachment);
    }

    public static Future<Buffer> fileDownload(final JsonObject attachment) {
        return AtFs.fileDownload(attachment);
    }

    public static Future<JsonArray> fileUpload(final JsonArray attachment) {
        return AtFs.fileUpload(attachment);
    }

    public static Future<JsonArray> fileRemove(final JsonArray attachment) {
        return AtFs.fileRemove(attachment);
    }

    public static Future<JsonArray> fileDir(final JsonArray attachment, final JsonObject params) {
        return AtFsDir.fileDir(attachment, params);
    }

    public static Future<JsonObject> fileMeta(final JsonObject appJ) {
        return AtFs.fileMeta(appJ);
    }

    public static List<XActivityChange> diffChange(final JsonObject recordO, final JsonObject recordN, final HAtom atom) {
        return AtDiffer.diff(recordO, recordN, atom);
    }

    public static List<XActivityChange> diffChange(final List<XActivityChange> changes, final XActivity activity) {
        return AtDiffer.diff(changes, activity);
    }

    public interface LOG {
        String MODULE = "περιβάλλων";

        LogModule App = Log.modulat(MODULE).program("App");
        LogModule File = Log.modulat(MODULE).program("File");
        LogModule Flow = Log.modulat(MODULE).program("Flow");
        LogModule HES = Log.modulat(MODULE).program("HES");
        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Tabb = Log.modulat(MODULE).program("Tabb");
    }
}
