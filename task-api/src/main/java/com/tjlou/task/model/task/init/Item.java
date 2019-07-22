package com.tjlou.task.model.task.init;

import com.gaby.annotation.Field;
import lombok.Data;

import java.util.Date;

/**
*@discrption:查询订单信息以及appid
*@user:Gaby
*@createTime:2019-07-22 11:25
*/
@Data
public class Item {

    @Field(comment = "订单标识")
    private Long orderId;
    @Field(comment = "appid")
    private String appKey;
    @Field(comment = "发货时间")
    private Date consignTime;
}
