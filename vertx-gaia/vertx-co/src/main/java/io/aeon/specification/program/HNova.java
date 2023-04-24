package io.aeon.specification.program;

import io.aeon.atom.iras.HRepo;
import io.aeon.eon.em.RTEAeon;
import io.aeon.specification.action.HEvent;

import java.util.concurrent.ConcurrentMap;

/**
 * 「持续在线」
 * 执行者：新星
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HNova extends HEvent<ConcurrentMap<RTEAeon, HRepo>, Boolean> {

}
