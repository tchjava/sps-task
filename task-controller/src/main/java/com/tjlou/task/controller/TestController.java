package com.tjlou.task.controller;

import com.gaby.model.DefaultResponse;
import com.tjlou.task.list.NoPayOrderRunnable;
import com.tjlou.task.model.task.add.Request;
import com.tjlou.task.schedule.MerakTaskScheduler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TestController implements BeanFactoryAware {

    @Autowired
    private MerakTaskScheduler merakTaskScheduler;

    @Autowired
    private BeanFactory beanFactory;

    private NoPayOrderRunnable noPayOrderRunnable;

    @RequestMapping("add")
    public DefaultResponse add(@RequestBody Request request) {
        NoPayOrderRunnable noPayOrderRunnable = getNoPayOrderRunnable();
        noPayOrderRunnable.setId(request.getId());
        merakTaskScheduler.schedule(noPayOrderRunnable,request.getDate());
        return null;
    }

    @RequestMapping("reset")
    public DefaultResponse reset() {
        merakTaskScheduler.reset();
        return null;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public NoPayOrderRunnable getNoPayOrderRunnable() {
        noPayOrderRunnable= (NoPayOrderRunnable) beanFactory.getBean("noPayOrderRunnable");
        return noPayOrderRunnable;
    }
}
