package io.vertx.tp.crud.cv;

import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Angle;
import io.vertx.tp.crud.uca.output.Post;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Pooled {

    ConcurrentMap<String, Angle> ANGLE_MAP = new ConcurrentHashMap<>();

    ConcurrentMap<String, Pre> PRE_MAP = new ConcurrentHashMap<>();

    ConcurrentMap<String, Post> POST_MAP = new ConcurrentHashMap<>();
}
