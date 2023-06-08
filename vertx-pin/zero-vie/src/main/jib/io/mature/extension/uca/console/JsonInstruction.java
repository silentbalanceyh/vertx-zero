package io.mature.extension.uca.console;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.modello.dynamic.modular.file.AoFile;
import io.modello.dynamic.modular.file.ExcelReader;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

import static io.mature.extension.refine.Ox.LOG;

/**
 * （专用建模工具）
 * Excel 建模文件转换成 Json 的建模文件
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JsonInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput input) {
        final String appName = this.inString(input, "a");
        /*
         * appName为null，直接获取app
         */
        if (appName == null) {
            return Ok.app().compose(ok -> this.defaultValue(input, ok));
        } else {
            return Ok.vendor(appName).compose(okB -> this.defaultValue(input, okB.configApp()));
        }
    }

    private Future<TermStatus> defaultValue(final CommandInput input, final HArk ark) {
        final String inputPath = this.inString(input, "i");
        final String outPath = this.inString(input, "o");

        final AoFile reader = Ut.singleton(ExcelReader.class, inputPath);

        /*
         * 模型文件写入
         */
        final HApp app = ark.app();
        final Set<Model> models = reader.readModels(app.name());
        final Set<Schema> schemata = new HashSet<>();
        models.forEach(model -> {
            final JsonObject modelJson = model.toJson();
            final String resolved = this.outPath(outPath + "model", model.identifier());
            LOG.Hub.info(this.logger(), "写入模型（Model）：{0} -> {1}", model.identifier(), resolved);
            Ut.ioOut(resolved, modelJson);

            schemata.addAll(model.schema());
        });
        /*
         * 实体文件写入
         */
        schemata.forEach(schema -> {
            final JsonObject schemaJson = schema.toJson();
            final String resolved = this.outPath(outPath + "schema", schema.identifier());
            LOG.Hub.info(this.logger(), "写入实体（Schema）：{0} -> {1}", schema.identifier(), resolved);
            Ut.ioOut(resolved, schemaJson);
        });
        return Ux.future(TermStatus.SUCCESS);
    }

    private String outPath(final String folder, final String identifier) {
        final String absolutePath = this.inFolder(this.path(folder));
        return absolutePath + "/" + identifier + VString.DOT + VPath.SUFFIX.JSON;
    }
}
