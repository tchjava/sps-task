package com.tjlou.task.goods;

import com.gaby.annotation.Field;
import lombok.Data;

/**
*@discrption:商品相关数据
*@user:Gaby
*@createTime:2019-08-23 15:01
*/
@Data
public class GoodsModel {

    @Field(comment = "商品标识")
    private Long id;

    @Field(comment = "商品标题")
    private String title;

    @Field(comment = "代理价格")
    private Integer agentPrice;

    @Field(comment = "销售价格")
    private Integer price;

    @Field(comment = "店铺ID")
    private String shopUri;

    @Field(comment = "商品图片")
    private String firstPic;

    @Field(comment = "商户标识")

    private Long supplierId;

    @Field(comment = "版本号")
    private Integer version;

    @Field(comment = "是否平台推荐")
    private Boolean nominate;

    @Field(comment = "平台标识")
    private Long appId;

    @Field(comment = "库存")
    private int stock;

}
