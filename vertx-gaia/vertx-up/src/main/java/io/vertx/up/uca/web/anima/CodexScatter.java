package io.vertx.up.uca.web.anima;

import io.aeon.runtime.H2H;
import io.horizon.eon.info.VMessage;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.bridge.FileSuffix;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class CodexScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(CodexScatter.class);

    @Override
    public void connect(final Vertx vertx) {
        // 1. Load rules
        final List<String> rules = Ut.ioFiles("codex", FileSuffix.YML);
        LOGGER.info(VMessage.SCATTER_CODEX, rules.size());
        // 2. Load request from rules
        for (final String rule : rules) {
            try {
                final String filename = "codex/" + rule;
                final JsonObject ruleData = Ut.ioYaml(filename);
                if (null != ruleData && !ruleData.isEmpty()) {
                    // File the codex map about the rule definitions.
                    final ConcurrentMap<String, JsonObject> store = H2H.CC_CODEX.store();
                    store.put(rule.substring(0, rule.lastIndexOf(Strings.DOT)), ruleData);
                    // ZeroCodex.getCodex().put(rule.substring(0, rule.lastIndexOf(Strings.DOT)), ruleData);
                }
            } catch (final EmptyStreamException ex) {
                LOGGER.runtime(ex);
            }
        }
    }
}
