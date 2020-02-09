package com.tjlou.task.list;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gaby.lock.ContentionLock;
import com.gaby.util.DateUtil;
import com.tjlou.mybatis.auto.mysql.sps.entity.GoodsExtInfo;
import com.tjlou.mybatis.auto.mysql.sps.service.GoodsExtInfoService;
import com.tjlou.task.goods.WeightModel;
import com.tjlou.task.service.GoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.List;

/**
 * @discrption:计算商品的权重
 * @user:Gaby
 * @createTime:2019-08-29 14:32
 */
@Scope("prototype")
@Component
public class GoodsWeightRunnable implements Runnable {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsExtInfoService goodsExtInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    private String lockName = "goodsWeight";

    @Override
    public void run() {

        Jedis jedis = ((JedisConnection) redisTemplate.getConnectionFactory().getConnection()).getJedis();

        ContentionLock lock = new ContentionLock(jedis, lockName);

        try {
            lock.lock();

            //结束时间
            Date endTime = new Date();
            //开始时间
            Date startTime = DateUtil.addDay(endTime, -1);

            List<WeightModel> data = goodsService.queryGoodsWeightData(startTime, endTime);
            //得到脚本管理器
            ScriptEngine js = new ScriptEngineManager().getEngineByExtension("js");
            if (CollectionUtils.isNotEmpty(data)) {
                data.forEach(weightModel -> {
                    try {
                        weightModel.setScore(Double.valueOf(js.eval(weightModel.getCompute()).toString()));
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                    GoodsExtInfo update = new GoodsExtInfo();
                    update.setWeight(weightModel.getScore());
                    goodsExtInfoService.update(update, new EntityWrapper<GoodsExtInfo>().eq(GoodsExtInfo.GOODS_ID, weightModel.getGoodsId()));
                    goodsExtInfoService.updateById(update);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
            jedis.close();
        }
    }

}
