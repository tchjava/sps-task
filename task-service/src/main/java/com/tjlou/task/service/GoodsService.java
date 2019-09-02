package com.tjlou.task.service;

import com.tjlou.mybatis.auto.mysql.sps.entity.GoodsInfo;
import com.tjlou.mybatis.base.service.BaseService;
import com.tjlou.task.goods.WeightModel;

import java.util.Date;
import java.util.List;

public interface GoodsService extends BaseService<GoodsInfo> {
    /**
     * 根据时间区间来获取商品对应的权重公式
     * @param startTime
     * @param endTime
     * @return
     */
    List<WeightModel> queryGoodsWeightData(Date startTime, Date endTime);

}
