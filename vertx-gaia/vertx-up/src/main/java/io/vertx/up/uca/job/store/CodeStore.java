package io.vertx.up.uca.job.store;

import io.vertx.up.atom.worker.Mission;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Set;

/**
 * Bridge for different JobStore
 */
class CodeStore implements JobReader {
    private static final Set<Mission> MISSIONS = ZeroAnno.getJobs();

    @Override
    public Set<Mission> fetch() {
        return MISSIONS;
    }

    @Override
    public Mission fetch(final String code) {
        return Fn.runOr(null, () -> MISSIONS.stream()
            .filter(mission -> code.equals(mission.getCode()))
            .findFirst().orElse(null), code);
    }
}
