package com.tjlou.task.model.task.init;

import com.gaby.annotation.Field;
import lombok.Data;

import java.util.Date;

/**
*@discrption:可解冻信息
*@user:Gaby
*@createTime:2019-07-22 14:25
*/
@Data
public class ThawItem {

    @Field(comment = "余额标识")
    private Long balanceId;
    @Field(comment ="变动时间")
    private Date date;
}
