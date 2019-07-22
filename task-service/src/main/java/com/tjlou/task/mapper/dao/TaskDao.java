package com.tjlou.task.mapper.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.task.model.task.init.ApplicationItem;
import com.tjlou.task.model.task.init.Item;
import com.tjlou.task.model.task.init.RejectItem;
import com.tjlou.task.model.task.init.ThawItem;

import java.util.List;

public interface TaskDao extends BaseMapper<OrderInfo> {
    List<Item> queryReceiveOrder();

    List<ThawItem> queryThaw();

    List<ApplicationItem> queryApplicationRefundInfo();

    List<RejectItem> queryRejectRefundInfo();
}
