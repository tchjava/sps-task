package com.tjlou.task.mapper.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.task.model.task.init.Item;

import java.util.List;

public interface TaskDao extends BaseMapper<OrderInfo> {
    List<Item> queryReceiveOrder();
}
