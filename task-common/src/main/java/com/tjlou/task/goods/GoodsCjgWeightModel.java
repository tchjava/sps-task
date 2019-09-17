package com.tjlou.task.goods;

import com.gaby.annotation.Field;
import lombok.Data;

/**
*@discrption:计算超级购中商品的权重-实体类
*@user:Gaby
*@createTime:2019-09-17 11:59
*/
@Data
public class GoodsCjgWeightModel {

    @Field(comment = "商品标识")
    private Long goodsId;

    @Field(comment = "浏览数")
    private double clickNum;

    @Field(comment = "订单数")
    private double orderNum;
}
