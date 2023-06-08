package io.mature.extension.quiz.develop;

import cn.vertxup.ambient.service.application.InitStub;
import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.eon.em.Environment;
import io.horizon.fn.Actuator;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.migration.MigrateStep;
import io.mature.extension.migration.restore.MetaLimit;
import io.mature.extension.refine.Ox;
import io.mature.extension.stellaris.Ok;
import io.mature.extension.stellaris.OkA;
import io.modello.dynamic.modular.file.AoFile;
import io.modello.dynamic.modular.file.ExcelReader;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxTimer;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.mature.extension.refine.Ox.LOG;


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

    public static DevModeller instance(final String input, final String output) {
        return new DevModeller(input, output);
    }

    private String slashPath(final String literal) {
        if (Objects.nonNull(literal)) {
            if (literal.endsWith(VString.SLASH)) {
                return literal;
            } else {
                return literal + VString.SLASH;
            }
        } else {
            return null;
        }
    }

    public void preprocess() {
        this.preprocess(null);
    }

    public void preprocess(final Actuator actuator) {
        Ok.on(handler -> {
            final OkA partyA = handler.result();
            final HArk ark = partyA.configApp();
            final HApp app = ark.app();
            /*
             * Timer started
             */
            final UxTimer timer = Ux.Timer.on().start(System.currentTimeMillis());
            final AoFile reader = Ut.singleton(ExcelReader.class, this.input);
            final Set<Model> models = reader.readModels(app.name());
            final Set<Schema> schemata = new HashSet<>();
            /*
             * 1. Model loading
             * 2. Schema loading
             */
            models.forEach(model -> {
                final JsonObject modelJson = model.toJson();
                final String resolved = this.output + "model/" + model.identifier() + VString.DOT + VPath.SUFFIX.JSON;
                LOG.Hub.info(this.getClass(), "Writing Model: {0} -> {1}", model.identifier(), resolved);
                /*
                 * Flush data to output path
                 */
                Ut.ioOut(resolved, modelJson);
                schemata.addAll(model.schema());
            });
            schemata.forEach(schema -> {
                final JsonObject schemaJson = schema.toJson();
                final String resolved = this.output + "schema/" + schema.identifier() + VString.DOT + VPath.SUFFIX.JSON;
                LOG.Hub.info(this.getClass(), "Writing Entity: {0} -> {1}", schema.identifier(), resolved);
                Ut.ioOut(resolved, schemaJson);
            });
            /*
             * Timer end
             */
            timer.end(System.currentTimeMillis());
            LOG.Hub.info(this.getClass(), "Successfully generation: {0}", timer.value());
            if (Objects.isNull(actuator)) {
                System.exit(0);
            }
            actuator.execute();
        });
    }

    public void initialize() {
        this.initialize(null);
    }

    public void initialize(final Actuator actuator) {
        Ok.on(handler -> {
            final OkA partyA = handler.result();
            final HArk ark = partyA.configApp();
            final HApp app = ark.app();

            final InitStub stub = Ox.pluginInitializer();
            /*
             * Timer started
             */
            final UxTimer timer = Ux.Timer.on().start(System.currentTimeMillis());
            stub.initModeling(app.name(), this.output).compose(initialized -> {
                // #NEW_LOG
                LOG.Atom.info(this.getClass(), "Modeling Environment has been initialized!");
                final MigrateStep step = new MetaLimit(Environment.Development);
                return step.bind(ark).procAsync(new JsonObject());
            }).onSuccess(result -> {
                /*
                 * Timer end
                 */
                timer.end(System.currentTimeMillis());
                // #NEW_LOG
                LOG.Atom.info(this.getClass(), "Modeling Adjustment has been finished: {0}", timer.value());
                if (Objects.isNull(actuator)) {
                    System.exit(0);
                }
                actuator.execute();
            });
        });
    }
}
