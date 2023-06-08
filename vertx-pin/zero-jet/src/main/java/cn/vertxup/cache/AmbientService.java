package cn.vertxup.cache;

import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import cn.vertxup.jet.service.JobKit;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.atom.JtJob;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.init.JtPin;
import io.vertx.mod.jet.init.ServiceEnvironment;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.uca.job.plugin.JobClient;
import io.vertx.up.uca.job.plugin.JobInfix;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class AmbientService implements AmbientStub {

    @Override
    public Future<JsonObject> updateJob(final IJob job, final IService service) {
        /*
         * `io.vertx.mod.atom.jet.JtApp`
         * -- reference extracted from
         *    `io.horizon.environment.spi.AmbientOld`
         */
        final String sigma = job.getSigma();
        final HArk ark = Ke.ark(sigma);
        if (Objects.isNull(ark)) {
            /*
             * 500 XHeader Exception, could not be found
             */
            return null; // Fn.outWeb(true, CombineAppException.class, this.getClass(), sigma);
        }


        /*
         * JtJob combining
         */
        final JtJob instance = new JtJob(job, service).bind(ark);
        /*
         * XHeader Flush Cache
         */
        final HApp app = ark.app();
        final String appId = app.option(KName.APP_ID);
        final ServiceEnvironment environment = JtPin.serviceEnvironment().get(appId);
        environment.flushJob(instance);
        /*
         * Mission here for JobPool updating
         */
        final Mission mission = instance.toJob();
        /*
         * Reset `Status`
         */
        mission.setStatus(EmJob.Status.STOPPED);
        final JobClient client = JobInfix.getClient();
        client.save(mission);
        return Ux.future(JobKit.toJson(mission));
    }

    @Override
    public Future<JsonObject> updateUri(final IApi api, final IService service) {
        /*
         * `io.vertx.mod.atom.jet.JtApp`
         * -- reference extracted from
         *    `io.horizon.environment.spi.AmbientOld`
         */
        final String sigma = api.getSigma();
        final HArk ark = Ke.ark(sigma);
        if (Objects.isNull(ark)) {
            /*
             * 500 XHeader Exception, could not be found
             */
            return null; // Fn.outWeb(CombineAppException.class, this.getClass(), sigma);
        }

        /*
         * JtUri combining
         */
        final JtUri instance = new JtUri(api, service).bind(ark);
        /*
         * XHeader Flush Cache
         */
        final HApp app = ark.app();
        final String appId = app.option(KName.APP_ID);
        final ServiceEnvironment environment = JtPin.serviceEnvironment().get(appId);
        environment.flushUri(instance);
        /*
         * Response build
         */
        return Ux.future(instance.toJson());
    }
}
