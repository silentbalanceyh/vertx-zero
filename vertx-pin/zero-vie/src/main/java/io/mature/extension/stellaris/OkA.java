package io.mature.extension.stellaris;

import io.mature.extension.stellaris.vendor.OkB;
import io.vertx.up.atom.typed.UTenant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OkA extends OkX {

    boolean initialized();

    UTenant partyA();

    OkB partyB(String name);
}
