package com.tjlou.task.facade;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gaby.mq.QueueBean;
import com.gaby.util.DateUtil;
import com.tjlou.mybatis.auto.mysql.sps.entity.OrderInfo;
import com.tjlou.mybatis.auto.mysql.sps.service.OrderInfoService;
import com.tjlou.task.list.ConfirmTaskOrderRunnable;
import com.tjlou.task.list.JudgeRunnable;
import com.tjlou.task.list.NoPayOrderRunnable;
import com.tjlou.task.list.ThawRunnable;
import com.tjlou.task.model.task.init.ApplicationItem;
import com.tjlou.task.model.task.init.Item;
import com.tjlou.task.model.task.init.RejectItem;
import com.tjlou.task.model.task.init.ThawItem;
import com.tjlou.task.schedule.MerakTaskScheduler;
import com.tjlou.task.service.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${task.confirmTakeNotifyUrl}")
    private String confirmTakeNotifyUrl;

    @Value("${task.judgeTaskNotifyUrl}")
    private String judgeTaskNotifyUrl;

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
                confirmTaskOrderRunnable.setUrl(confirmTakeNotifyUrl);
                merakTaskScheduler.schedule(confirmTaskOrderRunnable, DateUtil.addMinute(DateUtil.addDay(item.getConsignTime(),7),1));
            });
        }

        //查询可以解冻的
        List<ThawItem> thawItems = taskService.queryThaw();
        if (CollectionUtils.isNotEmpty(thawItems)) {
            thawItems.stream().forEach(thawItem -> {
                ThawRunnable thawRunnable = (ThawRunnable) beanfactory.getBean("thawRunnable");
                QueueBean queueBean = new QueueBean();
                queueBean.setId(thawItem.getBalanceId());
                thawRunnable.setQueueBean(queueBean);
                merakTaskScheduler.schedule(thawRunnable,DateUtil.addMinute(DateUtil.addDay(thawItem.getDate(),7),1));
            });
        }
        //查询申请退款的记录 3天不处理就退款成功
        List<ApplicationItem> applicationItems = taskService.queryApplicationRefundInfo();
        if (CollectionUtils.isNotEmpty(applicationItems)) {
            applicationItems.stream().forEach(applicationItem -> {
                JudgeRunnable judgeRunnable = (JudgeRunnable) beanfactory.getBean("judgeRunnable");
                QueueBean queueBean = new QueueBean();
                queueBean.setId(applicationItem.getRefundId());
                queueBean.setAppKey(applicationItem.getAppKey());
                queueBean.setNotifyUrl(judgeTaskNotifyUrl);
                //设置拓展字段type:1-拒绝 2-退款
                queueBean.setExtend(JSONObject.parseObject("{type:2}"));
                judgeRunnable.setQueueBean(queueBean);
                merakTaskScheduler.schedule(judgeRunnable,DateUtil.addMinute(DateUtil.addDay(applicationItem.getModifyTime(),3),1));
            });
        }

        //查询拒绝中的记录 2天不处理就算退款关闭
        List<RejectItem> rejectItems = taskService.queryRejectRefundInfo();
        if (CollectionUtils.isNotEmpty(rejectItems)) {
            rejectItems.stream().forEach(rejectItem -> {
                JudgeRunnable judgeRunnable = (JudgeRunnable) beanfactory.getBean("judgeRunnable");
                QueueBean queueBean = new QueueBean();
                queueBean.setAppKey(rejectItem.getAppKey());
                queueBean.setId(rejectItem.getRefundId());
                queueBean.setNotifyUrl(judgeTaskNotifyUrl);
                queueBean.setExtend(JSONObject.parseObject("{type:1}"));
                //设置拓展字段type:1-拒绝 2-退款
                judgeRunnable.setQueueBean(queueBean);
                merakTaskScheduler.schedule(judgeRunnable,DateUtil.addMinute(DateUtil.addDay(rejectItem.getModifyTime(),3),1));
            });
        }
    }

    private BeanFactory beanfactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanfactory = beanFactory;

    }

}
