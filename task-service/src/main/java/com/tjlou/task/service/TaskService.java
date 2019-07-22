package com.tjlou.task.service;

import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.base.service.BaseService;
import com.tjlou.task.model.task.init.ApplicationItem;
import com.tjlou.task.model.task.init.Item;
import com.tjlou.task.model.task.init.RejectItem;
import com.tjlou.task.model.task.init.ThawItem;

import java.util.List;

public interface TaskService extends BaseService<OrderInfo> {
    /**
     * 查询已发货的订单
     * @return
     */
    List<Item> queryReceiveOrder();

    /**
     * 查询可以解冻的余额数据
     * @return
     */
    List<ThawItem> queryThaw();

    /**
     * 查询在申请退款中的退款数据
     * @return
     */
    List<ApplicationItem> queryApplicationRefundInfo();

    /**
     * 查询状态为卖家拒绝中的退款数据
     * @return
     */
    List<RejectItem> queryRejectRefundInfo();
}
