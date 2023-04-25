package io.vertx.tp.jet.uca.micro;

import io.horizon.specification.zero.action.Commercial;
import io.vertx.core.Future;
import io.vertx.tp.jet.monitor.JtMonitor;
import io.vertx.up.annotations.On;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.job.AbstractMission;

/**
 * 「Job Instance」
 * Configured in database, it's not need @Job
 * When the job has been stored into database, it's not need @Job, in this situation
 * zero system will ignore scanned this job instead of other implemention
 * <p>
 * The job scan
 * - CodeStore -> @Job
 * - ExtensionStore -> JtHypnos ( Database stored ) -> ( Job + Service )
 */
// @Job(value = JobType.CONTAINER)
public class JtThanatos extends AbstractMission {
    private transient final JtMonitor monitor = JtMonitor.create(this.getClass());

    /*
     * Data example
     * {
     *     "key" : "f723d571-39c8-4823-b0d6-82e15e463906",
     *     "service" : {
     *          "key" : "c77b751b-5b71-418d-abcc-6ed328f8e900",
     *          "namespace" : "cn.originx.vie.app.ex",
     *          "name" : "bastion.data-sync",
     *          "comment" : "堡垒机同步专用",
     *          "isWorkflow" : false,
     *          "isGraphic" : false,
     *          "channelType" : "ACTOR",
     *          "serviceRecord" : "cn.originx.bastion.data.BastionRecord",
     *          "serviceComponent" : "cn.originx.bastion.component.SyncComponent",
     *          "identifier" : "ci.bastion",
     *          "sigma" : "HGyhDZ5p96jaNFbpZMl6s6SJWD4PNzAm",
     *          "language" : "cn",
     *          "active" : true
     *     },
     *     "config" : null,
     *     "appId" : "86db806c-97a0-4173-9a6d-632cb1a82ac7",
     *     "_class" : "io.vertx.tp.jet.io.vertx.up.atom.JtJob",
     *     "job" : {
     *          "key" : "f723d571-39c8-4823-b0d6-82e15e463906",
     *          "namespace" : "cn.originx.vie.app.ex",
     *          "name" : "bastion.sync",
     *          "code" : "bastion.sync",
     *          "type" : "PLAN",
     *          "duration" : 5000,
     *          "proxy" : "io.vertx.tp.jet.uca.micro.JtThanatos",
     *          "serviceId" : "c77b751b-5b71-418d-abcc-6ed328f8e900",
     *          "sigma" : "HGyhDZ5p96jaNFbpZMl6s6SJWD4PNzAm"
     *     }
     * }
     */
    @On
    public Future<Envelop> start(final Envelop envelop,
                                 final Mission mission,
                                 final Commercial commercial,
                                 final Refer refer) {
        /*
         * Here provide another reference for Mission injection
         */
        return JtPandora.async(envelop, commercial, mission, refer, this.monitor);
    }
}
