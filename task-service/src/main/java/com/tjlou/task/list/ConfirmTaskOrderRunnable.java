package com.tjlou.task.list;

import com.gaby.annotation.Field;
import com.gaby.http.service.ApiService;
import com.gaby.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
*@discrption:确认收货任务
*@user:Gaby
*@createTime:2019-07-22 11:05
*/
@Scope("prototype")
@Component
public class ConfirmTaskOrderRunnable implements Runnable{

    @Field(comment = "订单标识")
    private Long orderId;

    @Field(comment = "平台标识")
    private String appKey;

    @Field(comment = "通知地址")
    private String url;

    @Autowired
    private ApiService apiService;


    public ConfirmTaskOrderRunnable(){}

    public ConfirmTaskOrderRunnable(Long id, String appKey,String url) {
        this.orderId=id;
        this.appKey = appKey;
        this.url = url;
    }
    @Override
    public void run() {
        try {
            String params = String.format("{platform:%s,orderId:%s}", DESUtil.desEncript(this.appKey,DESUtil.DEFAULT_KEY),this.orderId);
            apiService.doPost(this.url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
