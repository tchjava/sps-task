package com.tjlou.task.model.task.add;

import com.gaby.annotation.Field;
import lombok.Data;

import java.util.Date;

@Data
public class Request {
    @Field(comment = "执行规则表达式")
    private String cron;

    private Long id;
    private Date date;
}
