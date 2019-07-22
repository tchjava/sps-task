package com.tjlou.task.model.task.init;

import com.gaby.annotation.Field;
import lombok.Data;

import java.util.Date;

/**
*@discrption:退款状态为卖家拒绝中的记录类
*@user:Gaby
*@createTime:2019-07-22 15:25
*/
@Data
public class RejectItem {

    @Field(comment = "退款标识")
    private Long refundId;
    @Field(comment = "appid")
    private String appKey;
    @Field(comment = "修改时间")
    private Date modifyTime;

}
