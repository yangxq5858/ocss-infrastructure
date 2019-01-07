package com.ecmp.context;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaRepositories(basePackages = {"org.konghao"},
//        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class//指定自己的工厂类
//)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
