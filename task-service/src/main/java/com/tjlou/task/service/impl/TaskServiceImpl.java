package com.tjlou.task.service.impl;

import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.base.service.impl.BaseServiceImpl;
import com.tjlou.task.mapper.dao.TaskDao;
import com.tjlou.task.model.task.init.ApplicationItem;
import com.tjlou.task.model.task.init.Item;
import com.tjlou.task.model.task.init.RejectItem;
import com.tjlou.task.model.task.init.ThawItem;
import com.tjlou.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends BaseServiceImpl<TaskDao,OrderInfo> implements TaskService {
    @Override
    public List<Item> queryReceiveOrder() {
        return this.baseMapper.queryReceiveOrder();
    }

    @Override
    public List<ThawItem> queryThaw() {
        return this.baseMapper.queryThaw();
    }

    @Override
    public List<ApplicationItem> queryApplicationRefundInfo() {
        return this.baseMapper.queryApplicationRefundInfo();
    }

    @Override
    public List<RejectItem> queryRejectRefundInfo() {
        return this.baseMapper.queryRejectRefundInfo();
    }
}
