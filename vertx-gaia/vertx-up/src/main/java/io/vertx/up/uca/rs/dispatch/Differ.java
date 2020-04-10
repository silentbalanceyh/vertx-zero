package io.vertx.up.uca.rs.dispatch;

import io.vertx.up.atom.agent.Event;
import io.vertx.up.uca.rs.Aim;

/**
 * Different type for worklow building
 *
 * @param <Context>
 */
public interface Differ<Context> {

    Aim<Context> build(Event event);
}
