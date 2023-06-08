package io.vertx.up.backbone.dispatch;

import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;

/**
 * Different type for worklow building
 *
 * @param <Context>
 */
public interface Differ<Context> {

    Aim<Context> build(Event event);
}
