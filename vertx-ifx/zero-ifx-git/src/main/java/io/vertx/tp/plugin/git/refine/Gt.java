package io.vertx.tp.plugin.git.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;

/**
 * @author lang : 2023/4/26
 */
public final class Gt {
    public interface LOG {
        String INFIX = "πηγή";

        LogModule REPO = Log.modulat(INFIX).infix("Repo");
        LogModule COMMAND = Log.modulat(INFIX).infix("Command");
    }
}
