package cn.originx.uca.console;

import cn.originx.refine.Ox;
import cn.originx.scaffold.console.AbstractInstruction;
import cn.originx.stellaris.Ok;
import cn.originx.stellaris.vendor.OkB;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.vertx.up.atom.Refer;
import io.vertx.up.commune.Record;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.unity.Ux;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IndexInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        final String appName = this.inString(args, "a");
        return this.runEach(appName, identifier -> this.executeAsync(identifier, appName)).compose(done -> {
            Sl.output("全文检索索引全部创建完成，创建模型数量：{0}", done.size());
            return Ux.future(TermStatus.SUCCESS);
        });
    }

    private Future<Boolean> executeAsync(final String identifier, final String appName) {
        return Ok.ok().compose(okA -> {
            final JtApp app = okA.configApp();
            final OkB partyB = okA.partyB(appName);
            final DataAtom atom = Ao.toAtom(app.getName(), identifier);
            if (Objects.isNull(atom)) {
                /*
                 * Atom Modification
                 */
                return Ux.future(Boolean.FALSE);
            } else {
                final HDao dao = Ox.toDao(app.getAppId(), identifier);

                final ElasticSearchClient client = ElasticSearchInfix.getClient();
                final Refer recordRef = new Refer();
                /* 客户端 */
                return dao.fetchAllAsync()
                    /* 跳过索引创建流程 */
                    .compose(recordRef::future)

                    /* 翻译环节 */
                    .compose(nil -> partyB.fabric(atom))
                    .compose(fabric -> {

                        /* 引用提取 */
                        final Record[] records = recordRef.get();

                        /* 创建Es索引信息 */
                        Sl.output("准备创建索引：identifier = {0}, size = {1}",
                            identifier, String.valueOf(records.length));
                        final JsonArray documents = new JsonArray();
                        Arrays.stream(records).map(record -> fabric.inTo(record.toJson()).result())
                            .forEach(documents::add);
                        return Ux.future(client.createDocuments(identifier, documents));
                    });
            }
        });

    }
}
