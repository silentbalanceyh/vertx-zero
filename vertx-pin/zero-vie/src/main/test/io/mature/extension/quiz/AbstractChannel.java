package io.mature.extension.quiz;

import io.horizon.eon.VString;
import io.horizon.spi.jet.JtChannel;
import io.macrocosm.specification.app.HApp;
import io.mature.extension.quiz.atom.QRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.mod.jet.atom.JtCommercial;
import io.vertx.mod.jet.atom.JtConfig;
import io.vertx.mod.jet.atom.JtJob;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.init.JtPin;
import io.vertx.mod.jet.init.ServiceEnvironment;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.specification.action.Commercial;
import io.vertx.up.uca.job.center.Agha;
import io.vertx.up.uca.job.phase.Phase;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class AbstractChannel extends AbstractPlatform {
    /*
     * 重新构造Job和Uri
     */
    private static final ConcurrentMap<String, JtJob> JOBS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, JtUri> URIS = new ConcurrentHashMap<>();

    @Override
    public boolean setUpAfter(final TestContext context, final Async async) {
        final HApp app = this.ark().app();
        final String appId = app.option(KName.APP_ID);
        final ServiceEnvironment environment = JtPin.serviceEnvironment().get(appId);
        if (Objects.nonNull(environment)) {
            // 任务加载
            this.setUpJob(environment);
            // 接口加载
            this.setUpApi(environment);
            // 配置加载完成
            this.logger().info("[ Qz ] 通道配置加载完成！Environment = {0}", environment);
        }
        async.complete();
        return false;
    }

    private void setUpApi(final ServiceEnvironment environment) {
        if (URIS.isEmpty()) {
            final HApp app = this.ark().app();
            final JtConfig config = ZeroStore.option(YmlCore.router.__KEY, JtConfig.class, JtConfig::new);
            environment.routes().stream().map(api -> (JtUri) api.bind(config).bind(this.ark()))
                .forEach(api -> {
                    final HttpMethod method = api.method();
                    final String uri = api.path();
                    URIS.put(method + " " + uri, api);
                });
            this.logger().info("[ Qz ] ( Api ) 加载Api总数：{0}, sigma = {1}",
                String.valueOf(URIS.size()), app.option(KName.SIGMA));
        }
    }

    private void setUpJob(final ServiceEnvironment environment) {
        if (JOBS.isEmpty()) {
            environment.jobs().forEach(job -> {
                final Mission mission = job.toJob();
                JOBS.put(mission.getCode(), job);
            });
            final HApp app = this.ark().app();
            this.logger().info("[ Qz ] ( Job ) 加载Job总数：{0}, sigma = {1}",
                String.valueOf(JOBS.size()), app.option(KName.SIGMA));
        }
    }

    // -------------------- 子类专用 ---------------------

    protected Future<Envelop> tcScheduler(final String jobKey) {
        return this.channelJob(jobKey, job -> {
            final Mission mission = job.toJob();
            final Agha agha = Agha.get(mission.getType());
            Ut.contract(agha, Vertx.class, VERTX);
            return agha.begin(mission);
        }).compose(finished -> Ux.future(Envelop.success(finished)));
    }

    protected Future<Envelop> tcJob(final String jobKey) {
        return this.tcJob(jobKey, new JsonObject());
    }

    protected <T> Future<Envelop> tcJob(final String jobKey, final T input) {
        final JtChannel channel = this.channelJob(jobKey);
        final Envelop envelop = Envelop.success(input);
        return channel.transferAsync(envelop);
    }

    protected Future<Envelop> tcTask(final String jobKey) {
        return this.channelJob(jobKey, job -> {
            final Mission mission = job.toJob();
            final Phase phase = Phase.start(mission.getCode()).bind(VERTX).bind(mission);
            return Ux.future(mission)
                .compose(phase::inputAsync)
                .compose(phase::incomeAsync)
                .compose(phase::invokeAsync)
                .compose(phase::outcomeAsync)
                .compose(phase::outputAsync)
                .compose(phase::callbackAsync);
        });
    }

    protected Future<Envelop> tcApi(final String filename) {
        final QRequest request = this.inWeb(filename);
        return this.tcApi(request);
    }

    protected Future<Envelop> tcApi(final QRequest request) {
        final String apiKey = request.key();
        final JtChannel channel = this.channelApi(apiKey);
        {
            final JtUri uri = URIS.get(apiKey);
            request.mount(uri, this.ark());
        }
        final Envelop envelop = request.envelop();
        return channel.transferAsync(envelop);
    }

    // -------------------- 私有接口 ---------------------
    private JtChannel channel(final JtCommercial commercial) {
        commercial.bind(this.environment);
        final Class<?> channelCls = commercial.channelComponent();
        Objects.requireNonNull(channelCls);
        this.logger().info("[ Qz ] 通道类：{0}", channelCls.getName());
        final JtChannel channel = Ut.instance(channelCls);
        Ut.contract(channel, Commercial.class, commercial);
        return channel;
    }

    private JtChannel channelApi(final String uriKey) {
        final JtUri uri = URIS.get(uriKey);
        Objects.requireNonNull(uri);
        this.logger().info("[ Qz ] 读取通道使用的接口 key ：{0}", uriKey);
        return this.channel(uri);
    }

    private JtChannel channelJob(final String jobCode) {
        return this.channelJob(jobCode, job -> {
            final JtChannel channel = this.channel(job);
            Ut.contract(channel, Mission.class, job.toJob());
            return channel;
        });
    }

    private <T> T channelJob(final String jobCode, final Function<JtJob, T> executor) {
        final HApp app = this.ark().app();
        final String namespace = Ao.toNS(app.name());
        final String jobKey = namespace + VString.DASH + jobCode;
        final JtJob job = JOBS.get(jobKey);
        Objects.requireNonNull(job);
        this.logger().info("[ Qz ] 读取通道使用的任务 key ：{0}", jobKey);
        return executor.apply(job);
    }
}
