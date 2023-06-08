package io.mature.extension.uca.console;

import io.mature.extension.scaffold.console.AbstractInstruction;
import io.vertx.core.Future;
import io.vertx.up.plugin.shell.atom.CommandInput;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HEDInstruction extends AbstractInstruction {

    @Override
    public Future<TermStatus> executeAsync(final CommandInput input) {
        final String waitFor = this.inString(input, "i");
        final String path = this.inString(input, "p");
        final String console;
        if (Ut.isNil(path)) {
            console = Ut.encryptRSAP(waitFor);
        } else {
            final String publicKey = Ut.ioString(path);
            console = Ut.encryptRSAP(waitFor, publicKey);
        }
        System.out.println("加密前：" + waitFor);
        System.out.println("加密后：" + console);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(console), null);
        return Ux.future(TermStatus.SUCCESS);
    }
}
