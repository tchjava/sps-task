package com.tjlou.task.facade;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gaby.util.DateUtil;
import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.auto.mysql.sps.service.OrderInfoService;
import com.tjlou.task.list.ConfirmTaskOrderRunnable;
import com.tjlou.task.list.NoPayOrderRunnable;
import com.tjlou.task.model.task.init.Item;
import com.tjlou.task.schedule.MerakTaskScheduler;
import com.tjlou.task.service.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Component
public class TaskFacade implements BeanFactoryAware {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MerakTaskScheduler merakTaskScheduler;

    @Autowired
    private TaskService taskService;


    @PostConstruct
    public void init(){
        //查询待付款的订单
        List<OrderInfo> orderInfos = orderInfoService.selectList(new EntityWrapper<OrderInfo>().eq(OrderInfo.STATUS, 1));
        if (CollectionUtils.isNotEmpty(orderInfos)) {
            orderInfos.stream().filter(x-> null!=x.getExpire()).forEach(orderInfo -> {
                //订单未支付的任务对象
                NoPayOrderRunnable noPayOrderRunnable= (NoPayOrderRunnable) beanfactory.getBean("noPayOrderRunnable");
                noPayOrderRunnable.setId(orderInfo.getId());
                merakTaskScheduler.schedule(noPayOrderRunnable,orderInfo.getExpire());
            });
        }

        //查询可以自动收货的订单
        List<Item> items = taskService.queryReceiveOrder();
        if (CollectionUtils.isNotEmpty(items)) {
            items.stream().forEach(item->{
                ConfirmTaskOrderRunnable confirmTaskOrderRunnable = (ConfirmTaskOrderRunnable) beanfactory.getBean("confirmTaskOrderRunnable");
                confirmTaskOrderRunnable.setOrderId(item.getOrderId());
                confirmTaskOrderRunnable.setAppKey(item.getAppKey());
                merakTaskScheduler.schedule(confirmTaskOrderRunnable, DateUtil.addDay(item.getConsignTime(),7));
            });
        }


    }

    private BeanFactory beanfactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanfactory = beanFactory;

    }

}
