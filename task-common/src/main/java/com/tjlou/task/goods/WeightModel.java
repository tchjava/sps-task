package com.tjlou.task.goods;

import lombok.Data;

/**
*@discrption:商品权重接受实体
*@user:Gaby
*@createTime:2019-08-29 14:38
*/
@Data
public class WeightModel {

    private Long goodsId;

    private String compute;

    private double score;
}
