package com.tjlou.task.list;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.auto.mysql.sps.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
/**
*@discrption:未支付处理任务对象
*@user:Gaby
*@createTime:2019-07-22 11:05
*/
@Scope("prototype")
@Component
public class NoPayOrderRunnable implements Runnable{

    private Long id;
    @Autowired
    private OrderInfoService orderInfoService;

    public NoPayOrderRunnable() {}
    public NoPayOrderRunnable(Long id) {
        this.id=id;
    }

    @Override
    public void run() {
      OrderInfo orderInfo= orderInfoService.selectOne(new EntityWrapper<OrderInfo>().eq(OrderInfo.ID, id).eq(OrderInfo.STATUS, 1));
        if (null != orderInfo) {
            OrderInfo update = new OrderInfo();
            update.setId(id);
            //16-交易关闭
            update.setStatus(16);
            update.setUpdateTime(new Date());
            orderInfoService.updateById(update);
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
