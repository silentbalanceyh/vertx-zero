package io.vertx.mod.jet.uca.tunnel;

import io.horizon.spi.jet.JtComponent;
import io.vertx.core.Future;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class ActorChannel extends AbstractChannel {
    /*
     * Adaptor is pure to access dynamic database here, the specification is as:
     * Step 1:
     * - The component defined Database reference, it could be initialized
     * Step 2:
     * - The component defined Integration reference, it could be initialized
     * Step 3:
     * - The component defined Mission reference, it could be initialized
     */
    @Override
    public Future<Boolean> initAsync(final JtComponent component, final ActIn request) {
        return Ux.future(this.commercial())
            /*
             * Database initialized, Mount database to `JtComponent`
             */
            .compose(Anagogic::databaseAsync)
            .compose(database -> Ut.contractAsync(component, Database.class, database))
            /*
             * Integration inited, mount to `JtComponent`
             */
            .compose(dbed -> Ux.future(this.commercial().integration()))
            .compose(integration -> Ut.contractAsync(component, Integration.class, integration))
            /*
             * Mission inited, mount to `JtComponent`
             */
            .compose(dbed -> Ux.future(this.mission()))
            .compose(mission -> Ut.contractAsync(component, Mission.class, mission));
    }
}
