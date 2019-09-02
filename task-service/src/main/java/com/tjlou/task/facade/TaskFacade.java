package com.tjlou.task.facade;

import com.tjlou.task.list.GoodsWeightRunnable;
import com.tjlou.task.schedule.MerakTaskScheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Component
public class TaskFacade implements BeanFactoryAware {

    @Autowired
    private MerakTaskScheduler merakTaskScheduler;


    @PostConstruct
    public void init(){
        GoodsWeightRunnable goodsWeightRunnable = (GoodsWeightRunnable) beanfactory.getBean("goodsWeightRunnable");
        //0 0 5 * * ?
        merakTaskScheduler.schedule(goodsWeightRunnable,"0/10 * * * * ? ");
    }



    private BeanFactory beanfactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanfactory = beanFactory;

    }

}
