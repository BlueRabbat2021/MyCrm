package com.crm;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // spring整合junit
@ContextConfiguration({"classpath:applicationContext.xml"}) // 不启动服务器也能读取配置文件
public class BaseTest { // 定义一个父类，用于继承，子类就不用写这些注解了

}
