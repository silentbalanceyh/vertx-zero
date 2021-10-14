package cn.originx.stellaris;

import cn.originx.stellaris.vendor.OkB;
import io.vertx.up.atom.unity.UTenant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OkA extends OkX {

    boolean initialized();

    UTenant partyA();

    OkB partyB(String name);
}
