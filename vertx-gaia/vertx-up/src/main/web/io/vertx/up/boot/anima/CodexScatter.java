package io.vertx.up.boot.anima;

import io.aeon.runtime.CRunning;
import io.horizon.eon.VMessage;
import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.exception.internal.EmptyIoException;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

;

public class CodexScatter implements Scatter<Vertx> {

    private static final Annal LOGGER = Annal.get(CodexScatter.class);

    @Override
    public void connect(final Vertx vertx, final HConfig config) {
        // 1. Load rules
        final List<String> rules = Ut.ioFiles("codex", VPath.SUFFIX.YML);
        LOGGER.info(VMessage.Scatter.CODEX, rules.size());
        // 2. Load request from rules
        for (final String rule : rules) {
            try {
                final String filename = "codex/" + rule;
                final JsonObject ruleData = Ut.ioYaml(filename);
                if (null != ruleData && !ruleData.isEmpty()) {
                    // File the codex map about the rule definitions.
                    final ConcurrentMap<String, JsonObject> store = CRunning.CC_CODEX.store();
                    store.put(rule.substring(0, rule.lastIndexOf(VString.DOT)), ruleData);
                    // ZeroCodex.getCodex().put(rule.substring(0, rule.lastIndexOf(Strings.DOT)), ruleData);
                }
            } catch (final EmptyIoException ex) {
                LOGGER.fatal(ex);
            }
        }
    }
}
