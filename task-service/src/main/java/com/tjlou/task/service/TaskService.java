package com.tjlou.task.service;

import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.base.service.BaseService;
import com.tjlou.task.model.task.init.Item;

import java.util.List;

public interface TaskService extends BaseService<OrderInfo> {
    /**
     * 查询已发货的订单
     * @return
     */
    List<Item> queryReceiveOrder();
}
