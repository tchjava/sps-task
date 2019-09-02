package com.tjlou.task.mapper.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tjlou.mybatis.auto.mysql.sps.entity.GoodsInfo;
import com.tjlou.task.goods.WeightModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GoodsDao extends BaseMapper<GoodsInfo> {
    List<WeightModel> queryGoodsWeightData(@Param("startTime") Date startTime, @Param("endTime")Date endTime);
}
