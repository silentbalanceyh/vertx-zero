package io.mature.extension.uca.console;

import io.mature.extension.scaffold.console.AbstractInstruction;
import io.mature.extension.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.up.plugin.neo4j.Neo4jClient;
import io.vertx.up.plugin.neo4j.Neo4jInfix;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.plugin.shell.refine.Sl;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ResetNeo4jInstruction extends AbstractInstruction {
    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        return Ok.ok().compose(ok -> {
            final String group = this.inString(args, "g");
            /* 默认分组：__VERTX_ZERO__ */
            final Neo4jClient client = Neo4jInfix.getClient().connect(group);

            return client.graphicReset().compose(finished -> {
                Sl.output("Neo4j图库重置完成！");
                return Ux.future(TermStatus.SUCCESS);
            });
        });
    }
}
