package io.vertx.up.uca.job.store;

import io.vertx.tp.plugin.job.JobClient;
import io.vertx.tp.plugin.job.JobInfix;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.em.JobStatus;
import io.vertx.up.eon.em.JobType;
import io.vertx.up.log.Annal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bridge for different JobStore
 */
class UnityStore implements JobStore {

    private static final Annal LOGGER = Annal.get(UnityStore.class);
    /*
     * Code in programming here ( Could not modify, read-only )
     */
    private final transient JobReader reader = new CodeStore();
    /*
     * Storage for job definition ( Could be modified )
     */
    private final transient JobStore store = new ExtensionStore();
    /*
     * Job client
     */
    private final transient JobClient client = JobInfix.getClient();

    @Override
    public Set<Mission> fetch() {
        /*
         * Split all the job from here
         * 1) All programming jobs are readonly ( Fixed value )
         * 2) All stored jobs are editable ( Fixed value )
         * 3) Double check jobs `readOnly` here ( Be sure readOnly set correctly )
         */
        final Set<Mission> missions = this.reader.fetch()
                .stream()
                .filter(Mission::isReadOnly)
                .collect(Collectors.toSet());
        LOGGER.info(Info.JOB_SCANNED, missions.size(), "Programming");

        final Set<Mission> storage = this.store.fetch()
                .stream()
                .filter(mission -> !mission.isReadOnly())
                .collect(Collectors.toSet());
        LOGGER.info(Info.JOB_SCANNED, storage.size(), "Dynamic/Stored");

        /* Merged */
        final Set<Mission> result = new HashSet<>();
        result.addAll(missions);
        result.addAll(storage);

        /*
         * Status Modification for ONCE
         * */
        result.stream()
                .filter(mission -> JobType.ONCE == mission.getType())
                /*
                 * Once work is in `STARTING`, because it won't start
                 * We must convert `STARTING` to `STOPPED` to stop the job
                 * at that time here.
                 */
                .filter(mission -> JobStatus.STARTING == mission.getStatus())
                .forEach(mission -> mission.setStatus(JobStatus.STOPPED));

        /* Job Pool Sync */
        this.client.save(result);
        return result;
    }

    @Override
    public JobStore add(final Mission mission) {
        this.client.save(mission);
        return this.store.add(mission);
    }

    @Override
    public Mission fetch(final String code) {
        Mission mission = this.client.fetch(code);
        if (Objects.isNull(mission)) {
            mission = this.reader.fetch(code);
            if (Objects.isNull(mission)) {
                mission = this.store.fetch(code);
            }
        }
        return mission;
    }

    @Override
    public JobStore remove(final Mission mission) {
        this.client.remove(mission.getCode());
        return this.store.remove(mission);
    }

    @Override
    public JobStore update(final Mission mission) {
        this.client.save(mission);
        return this.store.update(mission);
    }
}
