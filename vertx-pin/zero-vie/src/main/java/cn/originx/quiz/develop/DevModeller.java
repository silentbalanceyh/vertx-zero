package cn.originx.quiz.develop;

import cn.originx.migration.MigrateStep;
import cn.originx.migration.restore.MetaLimit;
import cn.originx.refine.Ox;
import cn.originx.stellaris.Ok;
import cn.originx.stellaris.OkA;
import cn.vertxup.ambient.service.application.InitStub;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.modular.file.AoFile;
import io.vertx.tp.modular.file.ExcelReader;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxTimer;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DevModeller {
    private final transient String input;
    private final transient String output;

    DevModeller(final String input, final String output) {
        this.input = this.slashPath(input);
        this.output = this.slashPath(output);
    }

    private String slashPath(final String literal) {
        if (Objects.nonNull(literal)) {
            if (literal.endsWith(Strings.SLASH)) {
                return literal;
            } else {
                return literal + Strings.SLASH;
            }
        } else {
            return null;
        }
    }

    public void preprocess() {
        Ok.on(handler -> {
            final OkA partyA = handler.result();
            final JtApp app = partyA.configApp();
            /*
             * Timer started
             */
            final UxTimer timer = Ux.Timer.on().start(System.currentTimeMillis());
            final AoFile reader = Ut.singleton(ExcelReader.class, this.input);
            final Set<Model> models = reader.readModels(app.getName());
            final Set<Schema> schemata = new HashSet<>();
            /*
             * 1. Model loading
             * 2. Schema loading
             */
            models.forEach(model -> {
                final JsonObject modelJson = model.toJson();
                final String resolved = this.output + "model/" + model.identifier() + Strings.DOT + FileSuffix.JSON;
                Ox.Log.infoHub(this.getClass(), "Writing Model: {0} -> {1}", model.identifier(), resolved);
                /*
                 * Flush data to output path
                 */
                Ut.ioOut(resolved, modelJson);
                schemata.addAll(model.schema());
            });
            schemata.forEach(schema -> {
                final JsonObject schemaJson = schema.toJson();
                final String resolved = this.output + "schema/" + schema.identifier() + Strings.DOT + FileSuffix.JSON;
                Ox.Log.infoHub(this.getClass(), "Writing Entity: {0} -> {1}", schema.identifier(), resolved);
                Ut.ioOut(resolved, schemaJson);
            });
            /*
             * Timer end
             */
            timer.end(System.currentTimeMillis());
            Ox.Log.infoHub(this.getClass(), "Successfully generation: {0}", timer.value());
            System.exit(0);
        });
    }

    public void initialize() {
        Ok.on(handler -> {
            final OkA partyA = handler.result();
            final JtApp app = partyA.configApp();

            final InitStub stub = Ox.pluginInitializer();
            /*
             * Timer started
             */
            final UxTimer timer = Ux.Timer.on().start(System.currentTimeMillis());
            stub.initModeling(app.getName()).compose(initialized -> {
                Ox.Log.infoAtom(this.getClass(), "Modeling Environment has been initialized!");
                final MigrateStep step = new MetaLimit(Environment.Development);
                return step.bind(app).procAsync(new JsonObject());
            }).onSuccess(result -> {
                /*
                 * Timer end
                 */
                timer.end(System.currentTimeMillis());
                Ox.Log.infoAtom(this.getClass(), "Modeling Adjustment has been finished: {0}", timer.value());
                System.exit(0);
            });
        });
    }
}
