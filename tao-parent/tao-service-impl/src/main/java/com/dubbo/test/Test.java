package com.dubbo.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
//import com.alibaba.dubbo.container.Main;


//模拟服务提供者，这里注意Main不要导错包,要引dubbo的包
//使用这种方式启动dubbo的provider要求dubbo的配置文件必须在类路径下的META-INF/spring/*.xml
public class Test {
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-spring.xml");
        context.start();
        System.out.println("provider start");
        System.in.read();
	}
}