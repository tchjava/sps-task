package com.tjlou.task.list;

import com.gaby.mq.QueueBean;
import com.tjlou.mybatis.auto.mysql.sps.entity.BillBalanceInfo;
import com.tjlou.mybatis.auto.mysql.sps.entity.BillBalanceLog;
import com.tjlou.mybatis.auto.mysql.sps.service.BillBalanceInfoService;
import com.tjlou.mybatis.auto.mysql.sps.service.BillBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
*@discrption:余额解冻任务对象
*@user:Gaby
*@createTime:2019-07-22 11:07
*/
@Scope("prototype")
@Component
public class ThawRunnable implements Runnable{

    private QueueBean queueBean;

    @Autowired
    private BillBalanceInfoService billBalanceInfoService;
    @Autowired
    private BillBalanceLogService billBalanceLogService;


    public ThawRunnable(){}
    public ThawRunnable(QueueBean queueBean) {
        this.queueBean = queueBean;
    }

    @Override
    public void run() {
        //根据余额标识查询余额信息
        BillBalanceLog balanceLog = billBalanceLogService.selectById(queueBean.getId());
        BillBalanceInfo billBalanceInfo = billBalanceInfoService.selectById(balanceLog.getBalanceId());
        //00D -超消保金冻结  解冻时 总额不增加 可用余额增加
        if (null != balanceLog && "00D".equals(balanceLog.getStatus())) {
            BillBalanceInfo update = new BillBalanceInfo();
            update.setId(billBalanceInfo.getId());
            update.setUsableBalance(billBalanceInfo.getUsableBalance()+balanceLog.getChangeNum());
            update.setModifyTime(new Date());
            billBalanceInfoService.updateById(update);

            //更新日志
            BillBalanceLog balanceLogUpdate = new BillBalanceLog();
            balanceLogUpdate.setId(balanceLog.getId());
            //2-收款
            balanceLogUpdate.setChangeType(2);
            balanceLogUpdate.setBlanceNum(billBalanceInfo.getBalance());
            balanceLogUpdate.setChangeTime(new Date());
            balanceLogUpdate.setStatus("00A");
            balanceLogUpdate.setComment("订单标识-"+balanceLogUpdate.getSourceId()+"确认后七天自动解冻");
            billBalanceLogService.updateById(balanceLogUpdate);
        }
    }

    public void setQueueBean(QueueBean queueBean) {
        this.queueBean = queueBean;
    }
}
