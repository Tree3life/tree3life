package com.tree3.controller;

import com.tree3.Tree3ChatApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/9 9:46 </p>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tree3ChatApplication.class)
public class 多例使用测试Test {

    @Lazy
    @Autowired
    private DemoBean demoBean;
    @Autowired
    private DemoBean demoBean2;

    @Autowired
    private ObjectFactory<DemoBean> demoBeanObjectFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testDemoBean() {
        System.out.println("xxx" + demoBean);
        System.out.println("xxx" + demoBean);
    }

    @Test
    public void testDemoBean2() {
        System.out.println("xxx" + demoBean2);
        System.out.println("xxx" + demoBean2);
    }

    @Test
    public void ccc() {
        System.out.println("factory1:"+demoBeanObjectFactory.getObject());
        System.out.println("factory2:"+demoBeanObjectFactory.getObject());
    }


    @Test
    public void ddd() {
        DemoBean bean1 = applicationContext.getBean(DemoBean.class);
        System.out.println(bean1);
        DemoBean bean2 = applicationContext.getBean(DemoBean.class);
        System.out.println(bean2);
    }

}