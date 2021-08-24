package io.vertx.tp.crud.actor;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/*
 * Actor workflow for each Envelop
 */
@Deprecated
public interface IxActor {

    static IxActor user() {
        return Fn.pool(Pool.ACTOR_MAP, UserActor.class.getName(),
                UserActor::new);
    }

    static IxActor uri() {
        return Fn.pool(Pool.ACTOR_MAP, UriActor.class.getName(),
                UriActor::new);
    }

    static IxActor view() {
        return Fn.pool(Pool.ACTOR_MAP, ViewActor.class.getName(),
                ViewActor::new);
    }

    static IxActor actor() {
        return Fn.pool(Pool.ACTOR_MAP, ModuleActor.class.getName(),
                ModuleActor::new);
    }

    static IxActor apeak() {
        return Fn.pool(Pool.ACTOR_MAP, ApeakActor.class.getName(),
                ApeakActor::new);
    }

    static IxActor unique() {
        return Fn.pool(Pool.ACTOR_MAP, UniqueActor.class.getName(),
                UniqueActor::new);
    }

    static IxActor header() {
        return Fn.pool(Pool.ACTOR_MAP, HeaderActor.class.getName(),
                HeaderActor::new);
    }

    static IxActor verify() {
        return Fn.pool(Pool.ACTOR_MAP, VerifyActor.class.getName(),
                VerifyActor::new);
    }

    static IxActor uuid() {
        return Fn.pool(Pool.ACTOR_MAP, UuidActor.class.getName(),
                UuidActor::new);
    }

    static IxActor key() {
        return Fn.pool(Pool.ACTOR_MAP, KeyActor.class.getName(),
                KeyActor::new);
    }

    static IxActor create() {
        return Fn.pool(Pool.ACTOR_MAP, CreateActor.class.getName(),
                CreateActor::new);
    }

    static IxActor update() {
        return Fn.pool(Pool.ACTOR_MAP, UpdateActor.class.getName(),
                UpdateActor::new);
    }

    static IxActor serial() {
        return Fn.pool(Pool.ACTOR_MAP, SerialActor.class.getName(),
                SerialActor::new);
    }

    static Future<JsonObject> start() {
        return Ux.future(new JsonObject());
    }

    /*
     * Input data here ( Async )
     */
    default Future<JsonObject> procAsync(final JsonObject data,
                                         final KModule config) {
        try {
            return Ux.future(this.proc(data, config));
        } catch (final WebException error) {
            return Future.failedFuture(error);
        }
    }

    /*
     * Input data here
     */
    JsonObject proc(JsonObject data, KModule config);

    /*
     * Bind
     */
    IxActor bind(Envelop envelop);
}
