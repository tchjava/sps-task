package com.tjlou.task.service.impl;

import com.tjlou.mybatis.auto.mysql.sps.entity.GoodsInfo;
import com.tjlou.mybatis.base.service.impl.BaseServiceImpl;
import com.tjlou.task.goods.GoodsCjgWeightModel;
import com.tjlou.task.goods.WeightModel;
import com.tjlou.task.mapper.dao.GoodsDao;
import com.tjlou.task.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsDao,GoodsInfo> implements GoodsService {
    @Override
    public List<WeightModel> queryGoodsWeightData(Date startTime, Date endTime) {
        return this.baseMapper.queryGoodsWeightData(startTime,endTime);
    }

    @Override
    public List<GoodsCjgWeightModel> queryGoodsCjgWeight(Date startTime, Date endTime) {
        return this.baseMapper.queryGoodsCjgWeight(startTime,endTime);
    }
}
