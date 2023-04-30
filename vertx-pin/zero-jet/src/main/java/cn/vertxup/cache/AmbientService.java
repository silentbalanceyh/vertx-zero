package cn.vertxup.cache;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import cn.vertxup.jet.service.JobKit;
import io.horizon.eon.em.scheduler.JobStatus;
import io.horizon.spi.environment.Ambient;
import io.horizon.spi.environment.AmbientEnvironment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500EnvironmentException;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.atom.JtJob;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.job.plugin.JobClient;
import io.vertx.up.uca.job.plugin.JobInfix;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class AmbientService implements AmbientStub {

    @Override
    public Future<JsonObject> updateJob(final IJob job, final IService service) {
        /*
         * `io.vertx.tp.jet.atom.JtApp`
         * -- reference extracted from
         *    `io.horizon.spi.environment.Ambient`
         */
        final String sigma = job.getSigma();
        final JtApp app = Ambient.getApp(sigma);
        if (Objects.isNull(app)) {
            /*
             * 500 XHeader Exception, could not be found
             */
            return Fn.outWeb(_500EnvironmentException.class, this.getClass(), sigma);
        } else {
            /*
             * JtJob combining
             */
            final JtJob instance = new JtJob(job, service).bind(app.getAppId());
            /*
             * XHeader Flush Cache
             */
            final AmbientEnvironment environment =
                Ambient.getEnvironments().get(app.getAppId());
            environment.flushJob(instance);
            /*
             * Mission here for JobPool updating
             */
            final Mission mission = instance.toJob();
            /*
             * Reset `JobStatus`
             */
            mission.setStatus(JobStatus.STOPPED);
            final JobClient client = JobInfix.getClient();
            client.save(mission);
            return Ux.future(JobKit.toJson(mission));
        }
    }

    @Override
    public Future<JsonObject> updateUri(final IApi api, final IService service) {
        /*
         * `io.vertx.tp.jet.atom.JtApp`
         * -- reference extracted from
         *    `io.horizon.spi.environment.Ambient`
         */
        final String sigma = api.getSigma();
        final JtApp app = Ambient.getApp(sigma);
        if (Objects.isNull(app)) {
            /*
             * 500 XHeader Exception, could not be found
             */
            return Fn.outWeb(_500EnvironmentException.class, this.getClass(), sigma);
        } else {
            /*
             * JtUri combining
             */
            final JtUri instance = new JtUri(api, service).bind(app.getAppId());
            /*
             * XHeader Flush Cache
             */
            final AmbientEnvironment environment =
                Ambient.getEnvironments().get(app.getAppId());
            environment.flushUri(instance);
            /*
             * Response build
             */
            return Ux.future(instance.toJson());
        }
    }
}
