package io.mature.extension.quiz.atom;

import io.horizon.eon.em.Environment;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.stellaris.OkA;
import io.mature.extension.stellaris.vendor.OkB;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.typed.UTenant;
import io.vertx.up.commune.config.Database;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QOk implements OkA {
    private static final String FILE_INTEGRATION = "environment/integration/";
    private static final String FILE_DATABASE = "environment/database.json";
    private final transient Environment environment;
    private final transient OkA ok;
    private final Cc<String, Database> CC_DB = Cc.open();

    private QOk(final OkA ok, final Environment environment) {
        this.environment = environment;
        this.ok = ok;
    }

    public static OkA create(final OkA ok, final Environment environment) {
        return new QOk(ok, environment);
    }

    @Override
    public boolean initialized() {
        return this.ok.initialized();
    }

    @Override
    public UTenant partyA() {
        return this.ok.partyA().copy();
    }

    @Override
    public OkB partyB(final String name) {
        final OkB okB = this.ok.partyB(name).copy();
        if (Environment.Mockito == this.environment) {
            final JsonObject item = Ut.ioJObject(FILE_INTEGRATION + name + ".json");
            if (Ut.isNotNil(item)) {
                okB.configIntegration().fromJson(item);
                okB.configIntegration().mockOn();
            }
        }
        return okB;
    }

    @Override
    public Database configDatabase() {
        final Database configDatabase = this.ok.configDatabase();
        return this.CC_DB.pick(() -> {
            final Database database = this.ok.configDatabase().copy();
            if (Environment.Mockito == this.environment) {
                final JsonObject item = Ut.ioJObject(FILE_DATABASE);
                if (Ut.isNotNil(item)) {
                    database.fromJson(item);
                }
            }
            return database;
        }, configDatabase.getJdbcUrl());
    }

    @Override
    public HArk configApp() {
        return this.ok.configApp();
    }
}
