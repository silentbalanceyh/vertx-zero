package io.mature.extension.uca.console;

import io.horizon.atom.common.Refer;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.mature.extension.stellaris.vendor.OkB;
import io.modello.specification.HRecord;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.up.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
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
            final HArk ark = okA.configApp();
            final HApp app = ark.app();

            final OkB partyB = okA.partyB(appName);
            final DataAtom atom = Ao.toAtom(app.name(), identifier);
            if (Objects.isNull(atom)) {
                /*
                 * Atom Modification
                 */
                return Ux.future(Boolean.FALSE);
            } else {
                final HDao dao = Ox.toDao(app.option(KName.APP_ID), identifier);

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
                        final HRecord[] records = recordRef.get();

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
