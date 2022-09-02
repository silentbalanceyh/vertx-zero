package io.vertx.up.uca.job.plugin;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.JobStatus;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Job Pool in memory or storage
 * Static pool for sync here.
 */
class JobPool {

    private static final Annal LOGGER = Annal.get(JobPool.class);

    private static final ConcurrentMap<String, Mission> JOBS = new ConcurrentHashMap<>();
    /* RUNNING Reference */
    private static final ConcurrentMap<Long, String> RUNNING = new ConcurrentHashMap<>();

    static void remove(final String code) {
        JOBS.remove(code);
    }

    static void save(final Mission mission) {
        if (Objects.nonNull(mission)) {
            JOBS.put(mission.getCode(), mission);
        }
    }

    static void bind(final Long timeId, final String code) {
        RUNNING.put(timeId, code);
    }


    static List<Mission> get() {
        return JOBS.values().stream()
            .filter(Objects::nonNull)
            .toList();
    }

    static Mission get(final String code) {
        return JOBS.get(code);
    }

    /*
     * Started job
     * --> RUNNING
     */
    static void start(final Long timeId, final String code) {
        uniform(code, mission -> {
            final JobStatus status = mission.getStatus();
            if (JobStatus.RUNNING == status) {
                /*
                 * If `RUNNING`
                 * Do not started here because it's running now
                 */
                LOGGER.info(Info.IS_RUNNING, code);
            } else if (JobStatus.ERROR == status) {
                /*
                 * If `ERROR`
                 * Could not started here
                 */
                LOGGER.warn(Info.IS_ERROR, code);
            } else if (JobStatus.STARTING == status) {
                /*
                 * If `STARTING`
                 * Could not started here
                 */
                LOGGER.warn(Info.IS_STARTING, code);
            } else {
                if (JobStatus.STOPPED == status) {
                    /*
                     * STOPPED -> READY
                     */
                    JOBS.get(code).setStatus(JobStatus.READY);
                }
                RUNNING.put(timeId, code);

            }
        });
    }

    /* Stop job --> Package Range */
    static void stop(final Long timeId) {
        uniform(timeId, mission -> {
            final JobStatus status = mission.getStatus();
            if (JobStatus.RUNNING == status || JobStatus.READY == status) {
                /*
                 * If `RUNNING`
                 * stop will trigger from
                 * RUNNING -> STOPPED
                 */
                RUNNING.remove(timeId);
                mission.setStatus(JobStatus.STOPPED);
            } else {
                /*
                 * Other status is invalid
                 */
                LOGGER.info(Info.NOT_RUNNING, mission.getCode(), status);
            }
        });
    }

    /* Package Range */
    static void resume(final Long timeId) {
        uniform(timeId, mission -> {
            final JobStatus status = mission.getStatus();
            if (JobStatus.ERROR == status) {
                /*
                 * If `ERROR`
                 * resume will be triggered
                 * ERROR -> READY
                 */
                RUNNING.put(timeId, mission.getCode());
                mission.setStatus(JobStatus.READY);
            }
        });
    }

    static JsonObject status(final String namespace) {
        /*
         * Revert
         */
        final ConcurrentMap<String, Long> runsRevert =
            new ConcurrentHashMap<>();
        RUNNING.forEach((timer, code) -> runsRevert.put(code, timer));
        final JsonObject response = new JsonObject();
        JOBS.forEach((code, mission) -> {
            /*
             * Processing
             */
            final JsonObject instance = new JsonObject();
            instance.put(KName.NAME, mission.getName());
            instance.put(KName.STATUS, mission.getStatus().name());
            /*
             * Timer
             */
            instance.put(KName.TIMER, runsRevert.get(mission.getCode()));
            response.put(mission.getCode(), instance);
        });
        return response;
    }

    static String code(final Long timeId) {
        return RUNNING.get(timeId);
    }

    /* Package Range */
    static Long timeId(final String code) {
        return RUNNING.keySet().stream()
            .filter(key -> code.equals(RUNNING.get(key)))
            .findFirst().orElse(null);
    }

    private static void uniform(final Long timeId, final Consumer<Mission> consumer) {
        final String code = RUNNING.get(timeId);
        if (Ut.isNil(code)) {
            LOGGER.info(Info.IS_STOPPED, timeId);
        } else {
            uniform(code, consumer);
        }
    }

    private static void uniform(final String code, final Consumer<Mission> consumer) {
        final Mission mission = JOBS.get(code);
        if (Objects.nonNull(mission)) {
            consumer.accept(mission);
        }
    }
}
