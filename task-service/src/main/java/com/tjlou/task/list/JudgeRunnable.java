package com.tjlou.task.list;

import com.gaby.http.service.ApiService;
import com.gaby.mq.QueueBean;
import com.gaby.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
*@discrption:同意退款的任务对象 (包含拒绝和同意)
*@user:Gaby
*@createTime:2019-07-22 13:58
*/
@Scope("prototype")
@Component
public class JudgeRunnable implements Runnable{

    private QueueBean queueBean;

    public JudgeRunnable(){}

    public JudgeRunnable(QueueBean queueBean) {
        this.queueBean = queueBean;
    }

    @Autowired
    private ApiService apiService;


    @Override
    public void run() {
        try {
            String params = String.format("{platform:%s,refundId:%s}",
                    DESUtil.desEncript(this.queueBean.getAppKey(),DESUtil.DEFAULT_KEY),this.queueBean.getId());
            apiService.doPost(this.queueBean.getNotifyUrl(), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setQueueBean(QueueBean queueBean) {
        this.queueBean = queueBean;
    }
}
