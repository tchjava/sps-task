package com.tjlou.task.mq.message;

import com.alibaba.fastjson.JSONObject;
import com.gaby.mq.QueueBean;
import com.tjlou.task.list.ConfirmTaskOrderRunnable;
import com.tjlou.task.list.NoPayOrderRunnable;
import com.tjlou.task.list.ThawRunnable;
import com.tjlou.task.schedule.MerakTaskScheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class CustomMessageListener implements MessageListener, BeanFactoryAware {

    @Autowired
    private MerakTaskScheduler merakTaskScheduler;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            QueueBean queueBean=JSONObject.parseObject(textMessage.getText(), QueueBean.class);
            switch (queueBean.getType()) {
                case 1://未支付
                    NoPayOrderRunnable noPayOrderRunnable= (NoPayOrderRunnable) beanFactory.getBean("noPayOrderRunnable");
                    noPayOrderRunnable.setId(queueBean.getId());
                    merakTaskScheduler.schedule(noPayOrderRunnable,queueBean.getDate());
                    break;
                case 2://7天自动收货
                    ConfirmTaskOrderRunnable confirmTaskOrderRunnable = (ConfirmTaskOrderRunnable) beanFactory.getBean("confirmTaskOrderRunnable");
                    confirmTaskOrderRunnable.setOrderId(queueBean.getId());
                    confirmTaskOrderRunnable.setAppKey(queueBean.getAppKey());
                    confirmTaskOrderRunnable.setUrl(queueBean.getNotifyUrl());
                    merakTaskScheduler.schedule(confirmTaskOrderRunnable,queueBean.getDate());
                    break;
                case 3://7天解除解冻
                    ThawRunnable thawRunnable = (ThawRunnable) beanFactory.getBean("thawRunnable");
                    thawRunnable.setBalanceId(queueBean.getId());
                    merakTaskScheduler.schedule(thawRunnable,queueBean.getDate());
                    break;
                case 4://卖家拒绝退款后，如买家不再申请退款，则 2天后取消。
                    break;
                case 5://买家申请退款后，卖家3天不处理，则退款成功。
                    break;
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
