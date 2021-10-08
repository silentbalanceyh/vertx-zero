package io.vertx.tp.rbac.authorization;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.authorization.child.GcCritical;
import io.vertx.tp.rbac.authorization.child.GcHorizon;
import io.vertx.tp.rbac.authorization.child.GcOverlook;
import io.vertx.tp.rbac.authorization.detent.*;
import io.vertx.tp.rbac.authorization.direct.GdCritical;
import io.vertx.tp.rbac.authorization.direct.GdHorizon;
import io.vertx.tp.rbac.authorization.direct.GdOverlook;
import io.vertx.tp.rbac.authorization.extend.GeCritical;
import io.vertx.tp.rbac.authorization.extend.GeHorizon;
import io.vertx.tp.rbac.authorization.extend.GeOverlook;
import io.vertx.tp.rbac.authorization.inherit.GiCritical;
import io.vertx.tp.rbac.authorization.inherit.GiHorizon;
import io.vertx.tp.rbac.authorization.inherit.GiOverlook;
import io.vertx.tp.rbac.authorization.parent.GpCritical;
import io.vertx.tp.rbac.authorization.parent.GpHorizon;
import io.vertx.tp.rbac.authorization.parent.GpOverlook;
import io.vertx.tp.rbac.logged.ProfileGroup;
import io.vertx.tp.rbac.logged.ProfileRole;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.List;

/*
 * Detent for ( ProfileType = Detent )
 *
 * Be careful:
 * If there existing constructor data parameter such as JsonObject or List<ProfileGroup>, it means
 * each time this object will stored single data here, in this kind of situation, we could not use
 * Pool.DETENT_POOL, if you used Pool.DETENT_POOL, the system will ignore
 * input parameter and used cached instead, it means that different user authorization may
 * shared the first time input parameters. It's wrong.
 *
 * There are some points:
 * 1. For tool object ( No constructor parameters ), we could cached ScDetent.
 * 2. For non tool object ( Input constructor parameters ), we mustn't cached ScDetent.
 */
public interface ScDetent {

    static ScDetent user(final JsonObject input) {
        return new ScDetentRole(input);
    }

    static ScDetent group(final JsonObject input) {
        return new ScDetentGroup(input);
    }

    static ScDetent parent(final JsonObject input,
                           final List<ProfileGroup> profiles) {
        return new ScDetentParent(input, profiles);
    }

    static ScDetent inherit(final JsonObject input,
                            final List<ProfileGroup> profiles) {
        return new ScDetentInherit(input, profiles);
    }

    static ScDetent extend(final JsonObject input,
                           final List<ProfileGroup> profiles) {
        return new ScDetentExtend(input, profiles);
    }

    static ScDetent children(final JsonObject input,
                             final List<ProfileGroup> profiles) {
        return new ScDetentChild(input, profiles);
    }

    JsonObject proc(List<ProfileRole> profiles);

    default Future<JsonObject> procAsync(final List<ProfileRole> profiles) {
        return Ux.future(this.proc(profiles));
    }

    /*
     * Internal default group
     */
    interface Group {

        static ScDetent horizon() {
            return Fn.pool(Pool.DETENT_POOL, GdHorizon.class.getName(), GdHorizon::new);
        }

        static ScDetent critical() {
            return Fn.pool(Pool.DETENT_POOL, GdCritical.class.getName(), GdCritical::new);
        }

        static ScDetent overlook() {
            return Fn.pool(Pool.DETENT_POOL, GdOverlook.class.getName(), GdOverlook::new);
        }

        /*
         * Group : Parent ( Exclude Current )
         */
        interface Parent {

            static ScDetent horizon() {
                return Fn.pool(Pool.DETENT_POOL, GpHorizon.class.getName(), GpHorizon::new);
            }

            static ScDetent critical(final List<ProfileGroup> original) {
                return new GpCritical(original);
            }

            static ScDetent overlook(final List<ProfileGroup> original) {
                return new GpOverlook(original);
            }
        }

        /*
         * Group : Child ( Exclude Current )
         */
        interface Child {
            static ScDetent horizon() {
                return Fn.pool(Pool.DETENT_POOL, GcHorizon.class.getName(), GcHorizon::new);
            }

            static ScDetent critical(final List<ProfileGroup> original) {
                return new GcCritical(original);
            }

            static ScDetent overlook(final List<ProfileGroup> original) {
                return new GcOverlook(original);
            }
        }

        interface Inherit {
            static ScDetent horizon(final List<ProfileGroup> original) {
                return new GiHorizon(original);
            }

            static ScDetent critical(final List<ProfileGroup> original) {
                return new GiCritical(original);
            }

            static ScDetent overlook(final List<ProfileGroup> original) {
                return new GiOverlook(original);
            }
        }

        interface Extend {
            static ScDetent horizon(final List<ProfileGroup> original) {
                return new GeHorizon(original);
            }

            static ScDetent critical(final List<ProfileGroup> original) {
                return new GeCritical(original);
            }

            static ScDetent overlook(final List<ProfileGroup> original) {
                return new GeOverlook(original);
            }
        }
    }
}
