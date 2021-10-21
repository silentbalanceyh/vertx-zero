package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface SettleStub {

    Future<List<FSettlement>> fetchByItems(List<FBillItem> items);
}