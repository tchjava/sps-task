package com.tjlou.task.list;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tjlou.mybatis.auto.mysql.sps.entity.BillBalanceInfo;
import com.tjlou.mybatis.auto.mysql.sps.entity.BillBalanceLog;
import com.tjlou.mybatis.auto.mysql.sps.service.BillBalanceInfoService;
import com.tjlou.mybatis.auto.mysql.sps.service.BillBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
*@discrption:余额解冻任务对象
*@user:Gaby
*@createTime:2019-07-22 11:07
*/
@Scope("prototype")
@Component
public class ThawRunnable implements Runnable{

    private Long balanceId;

    @Autowired
    private BillBalanceInfoService billBalanceInfoService;
    @Autowired
    private BillBalanceLogService billBalanceLogService;


    public ThawRunnable(){}
    public ThawRunnable(Long balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public void run() {
        //根据余额标识查询余额信息
        BillBalanceInfo billBalanceInfo = billBalanceInfoService.selectById(balanceId);
        //00D -超消保金冻结
        if (null != billBalanceInfo && "00D".equals(billBalanceInfo.getStatus())) {
            BillBalanceInfo update = new BillBalanceInfo();
            update.setId(billBalanceInfo.getId());
            update.setModifyTime(new Date());
            update.setStatus("00A");
            billBalanceInfoService.updateById(update);

            //根据用户标识查询最后的结余金额
           BillBalanceLog db_balanceLog= billBalanceLogService.selectOne(new EntityWrapper<BillBalanceLog>().eq(BillBalanceLog.USER_ACCOUNT_ID, billBalanceInfo.getUserAccountId())
                    .orderDesc(Arrays.asList(BillBalanceLog.CHANGE_TIME)));

           //添加到日志
            BillBalanceLog balanceLogInsert = new BillBalanceLog();
            balanceLogInsert.setBlanceId(update.getId());
            balanceLogInsert.setBlanceNum(db_balanceLog.getBlanceNum());
            balanceLogInsert.setChangeNum(billBalanceInfo.getBlance());
            balanceLogInsert.setChangeTime(update.getModifyTime());
            //2-收款
            balanceLogInsert.setChangeType(2);
            balanceLogInsert.setSourceId(billBalanceInfo.getBalanceRelaId());
            balanceLogInsert.setUserAccountId(billBalanceInfo.getUserAccountId());
            billBalanceLogService.insert(balanceLogInsert);
        }
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }
}
