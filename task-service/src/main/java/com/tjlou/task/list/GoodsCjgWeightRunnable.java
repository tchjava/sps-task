package com.tjlou.task.list;

import com.gaby.lock.ContentionLock;
import com.gaby.util.DateUtil;
import com.tjlou.mybatis.auto.mysql.sps.entity.GoodsExtInfo;
import com.tjlou.mybatis.auto.mysql.sps.service.GoodsExtInfoService;
import com.tjlou.task.goods.GoodsCjgWeightModel;
import com.tjlou.task.service.GoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

/**
*@discrption:计算超级购中商品的权重
*@user:Gaby
*@createTime:2019-09-17 10:59
*/
@Scope("prototype")
@Component
public class GoodsCjgWeightRunnable implements Runnable{

    private Logger logger = LoggerFactory.getLogger(GoodsCjgWeightRunnable.class);

    @Autowired
    private GoodsExtInfoService goodsExtInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    private String lockName = "goodsCjgWeight";


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

            //获取到每个商品的点击数和成交数
            List<GoodsCjgWeightModel> goodsCjgWeightModels = goodsService.queryGoodsCjgWeight(startTime,endTime);
            if (CollectionUtils.isNotEmpty(goodsCjgWeightModels)) {
                for (GoodsCjgWeightModel model : goodsCjgWeightModels) {
                    GoodsExtInfo update = new GoodsExtInfo();
                    update.setId(model.getGoodsId());
                    update.setGoodsId(model.getGoodsId());
                    // 权重值=订单数/点击数
                    update.setCjgWeight(model.getOrderNum() / model.getClickNum());
                    goodsExtInfoService.updateById(update);
                }
                logger.info("完成了超级购活动的商品权重计算");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            jedis.close();
        }
    }

}
